package com.jong.msa.board.microservice.post.service;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.member.feign.MemberFeignClient;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.ErrorCodeEnum.MemberErrorCode;
import com.jong.msa.board.core.feign.exception.FeignServiceException;
import com.jong.msa.board.core.redis.aspect.RedisCaching;
import com.jong.msa.board.core.redis.aspect.RedisRemove;
import com.jong.msa.board.core.transaction.aspect.DistributeTransaction;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.domain.post.entity.PostEntity;
import com.jong.msa.board.domain.post.repository.PostRepository;
import com.jong.msa.board.microservice.post.event.PostSaveEvent;
import com.jong.msa.board.microservice.post.exception.PostServiceException;
import com.jong.msa.board.microservice.post.mapper.PostEntityMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository repository;

	private final PostEntityMapper entityMapper;

	private final ApplicationEventPublisher eventPublisher;

	private final MemberFeignClient memberFeignClient;
	
	private <T> T getMemberByClientAfterProccess(UUID memberId, Function<MemberDetailsResponse, T> afterFunction) {
		
		try {
			
			return afterFunction.apply(memberFeignClient.getMember(memberId).getBody());
			
		} catch (FeignServiceException e) {
			
			String errorCode = e.getErrorResponse().getErrorCode();
			
			throw (MemberErrorCode.NOT_FOUND_MEMBER.getCode().equals(errorCode)) 
				? PostServiceException.notFoundPostWriter() 
				: RestServiceException.uncheckedError(HttpStatus.BAD_GATEWAY);
		}
	}
	
	@Transactional
	@Override
	public UUID createPost(CreatePostRequest request) {
		
		return getMemberByClientAfterProccess(request.getWriterId(), writer -> {
			
			PostEntity entity = entityMapper.toEntity(request);
			PostEntity savedEntity = repository.save(entity);
			
			eventPublisher.publishEvent(new PostSaveEvent(savedEntity.getId()));
			
			return savedEntity.getId();
		});
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.POST_LOCK_KEY, key = "#id")
	@RedisRemove(prefix = RedisKeyPrefixes.POST_KEY, key = "#id")
	@Override
	public void modifyPost(UUID id, ModifyPostRequest request) {
		
		PostEntity entity = repository.findById(id).orElseThrow(PostServiceException::notFoundPost);
		PostEntity savedEntity = repository.save(entityMapper.updateEntity(request, entity));

		eventPublisher.publishEvent(new PostSaveEvent(savedEntity.getId()));
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.POST_LOCK_KEY, key = "#id")
	@RedisRemove(prefix = RedisKeyPrefixes.POST_KEY, key = "#id")
	@Override
	public void increasePostViews(UUID id) {
		
		PostEntity entity = repository.findById(id).orElseThrow(PostServiceException::notFoundPost);
		PostEntity savedEntity = repository.save(entity.setViews(entity.getViews() + 1));

		eventPublisher.publishEvent(new PostSaveEvent(savedEntity.getId()));
	}

	@Transactional(readOnly = true)
	@RedisCaching(prefix = RedisKeyPrefixes.POST_KEY, key = "#id")
	@Override
	public PostDetailsResponse getPost(UUID id) {
		
		PostEntity entity = repository.findById(id).orElseThrow(PostServiceException::notFoundPost);
		
		return getMemberByClientAfterProccess(entity.getWriterId(), writer -> entityMapper.toResponse(entity, writer));
	}

}
