package com.jong.msa.board.microservice.post.service;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.core.utils.FeignExcuteUtils;
import com.jong.msa.board.client.member.feign.MemberFeignClient;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.redis.aspect.RedisCaching;
import com.jong.msa.board.core.redis.aspect.RedisRemove;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.core.web.response.ErrorResponse;
import com.jong.msa.board.domain.core.transaction.DistributeTransaction;
import com.jong.msa.board.domain.post.entity.PostEntity;
import com.jong.msa.board.domain.post.repository.PostRepository;
import com.jong.msa.board.microservice.post.event.PostSaveEvent;
import com.jong.msa.board.microservice.post.mapper.PostEntityMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository repository;

	private final PostEntityMapper entityMapper;

	private final ApplicationEventPublisher eventPublisher;

	private final MemberFeignClient memberFeignClient;
	
	@Transactional
	@Override
	public UUID createPost(CreatePostRequest request) {
		
		FeignExcuteUtils.Response<MemberDetailsResponse> memberDetailsResponse = 
				FeignExcuteUtils.excute(() -> memberFeignClient.getMember(request.getWriterId()));

		if (memberDetailsResponse.getError() != null) {

			ErrorResponse errorResponse = memberDetailsResponse.getError();
			
			if (ErrorCode.NOT_FOUND_MEMBER == errorResponse.getErrorCode()) {
				
				throw new RestServiceException(ErrorCode.NOT_FOUND_POST_WRITER);
				
			} else {

				throw new RestServiceException(ErrorCode.UNCHECKED_EXTERNAL_ERROR);
			}
		} else {
			
			PostEntity entity = entityMapper.toEntity(request);
			PostEntity savedEntity = repository.save(entity);

			eventPublisher.publishEvent(new PostSaveEvent(savedEntity.getId()));
			
			return savedEntity.getId();
		}
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.POST_LOCK_KEY, key = "#id")
	@RedisRemove(prefix = RedisKeyPrefixes.POST_KEY, key = "#id")
	@Override
	public void modifyPost(UUID id, ModifyPostRequest request) {
		
		PostEntity entity = repository.findById(id).orElseThrow(() -> new RestServiceException(ErrorCode.NOT_FOUND_POST));
		PostEntity savedEntity = repository.save(entityMapper.updateEntity(request, entity));

		eventPublisher.publishEvent(new PostSaveEvent(savedEntity.getId()));
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.POST_LOCK_KEY, key = "#id")
	@RedisRemove(prefix = RedisKeyPrefixes.POST_KEY, key = "#id")
	@Override
	public void increasePostViews(UUID id) {
		
		PostEntity entity = repository.findById(id).orElseThrow(() -> new RestServiceException(ErrorCode.NOT_FOUND_POST));
		PostEntity savedEntity = repository.save(entity.setViews(entity.getViews() + 1));

		eventPublisher.publishEvent(new PostSaveEvent(savedEntity.getId()));
	}

	@Transactional(readOnly = true)
	@RedisCaching(prefix = RedisKeyPrefixes.POST_KEY, key = "#id")
	@Override
	public PostDetailsResponse getPost(UUID id) {
		
		PostEntity entity = repository.findById(id).orElseThrow(() -> new RestServiceException(ErrorCode.NOT_FOUND_POST));

		FeignExcuteUtils.Response<MemberDetailsResponse> memberDetailsResponse = 
				FeignExcuteUtils.excute(() -> memberFeignClient.getMember(entity.getWriterId()));

		if (memberDetailsResponse.getError() != null) {

			ErrorResponse errorResponse = memberDetailsResponse.getError();
			
			if (ErrorCode.NOT_FOUND_MEMBER == errorResponse.getErrorCode()) {
				
				throw new RestServiceException(ErrorCode.NOT_FOUND_POST_WRITER);
				
			} else {

				throw new RestServiceException(ErrorCode.UNCHECKED_EXTERNAL_ERROR);
			}
		} else {
			
			return entityMapper.toResponse(entity, memberDetailsResponse.getBody());
		}
		
	}

}
