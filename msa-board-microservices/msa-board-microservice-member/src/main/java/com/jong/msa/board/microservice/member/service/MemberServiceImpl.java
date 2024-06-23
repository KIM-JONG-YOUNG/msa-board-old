package com.jong.msa.board.microservice.member.service;
 
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.member.request.MemberCreateRequest;
import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.common.enums.State;
import com.jong.msa.board.core.redis.aspect.RedisCaching;
import com.jong.msa.board.core.redis.aspect.RedisRemove;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.domain.core.transaction.DistributeTransaction;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.entity.QMemberEntity;
import com.jong.msa.board.domain.member.repository.MemberRepository;
import com.jong.msa.board.microservice.member.event.MemberSaveEvent;
import com.jong.msa.board.microservice.member.mapper.MemberEntityMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final PasswordEncoder encoder;
	
	private final MemberRepository repository;

	private final MemberEntityMapper entityMapper;
	
	private final ApplicationEventPublisher eventPublisher;

	private final JPAQueryFactory queryFactory;

	@DistributeTransaction(prefix = RedisKeyPrefixes.MEMBER_LOCK_KEY, key = "#request.username")
	@Override
	public UUID createMember(MemberCreateRequest request) {
		
		boolean isNotExistsUsername = queryFactory
				.selectOne()
				.from(QMemberEntity.memberEntity)
				.where(QMemberEntity.memberEntity.username.eq(request.getUsername()))
				.fetchOne() == null;

		if (isNotExistsUsername) {

			String encodedPassword = encoder.encode(request.getPassword());
			
			MemberEntity entity = entityMapper.toEntity(request);
			MemberEntity savedEntity = repository.save(entity.setPassword(encodedPassword));
			
			eventPublisher.publishEvent(new MemberSaveEvent(savedEntity.getId()));
			
			return savedEntity.getId();
		
		} else {

			throw new RestServiceException(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_USERNAME);
		}
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.MEMBER_LOCK_KEY, key = "#id")
	@RedisRemove(prefix = RedisKeyPrefixes.MEMBER_KEY, key = "#id")
	@Override
	public void modifyMember(UUID id, MemberModifyRequest request) {
		
		MemberEntity entity = repository.findById(id)
				.orElseThrow(() -> new RestServiceException(HttpStatus.GONE, ErrorCode.NOT_FOUND_MEMBER));
		MemberEntity savedEntity = repository.save(entityMapper.updateEntity(request, entity));
		
		eventPublisher.publishEvent(new MemberSaveEvent(savedEntity.getId()));
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.MEMBER_LOCK_KEY, key = "#id")
	@Override
	public void modifyMemberPassword(UUID id, MemberPasswordModifyRequest request) {
		
		MemberEntity entity = repository.findById(id)
				.orElseThrow(() -> new RestServiceException(HttpStatus.GONE, ErrorCode.NOT_FOUND_MEMBER));
		
		if (encoder.matches(request.getCurrentPassword(), entity.getPassword())) {

			String encodedPassword = encoder.encode(request.getNewPassword());

			MemberEntity savedEntity = repository.save(entity.setPassword(encodedPassword));

			eventPublisher.publishEvent(new MemberSaveEvent(savedEntity.getId()));

		} else {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_MATCHED_PASSWORD);
		}		
	}

	@Transactional(readOnly = true)
	@RedisCaching(prefix = RedisKeyPrefixes.MEMBER_KEY, key = "#id")
	@Override
	public MemberDetailsResponse getMember(UUID id) {
		
		MemberEntity entity = repository.findById(id)
				.orElseThrow(() -> new RestServiceException(HttpStatus.GONE, ErrorCode.NOT_FOUND_MEMBER));

		return entityMapper.toResponse(entity);
	}

	@Transactional(readOnly = true)
	@Override 
	public MemberDetailsResponse loginMember(MemberLoginRequest request) {
		
		MemberEntity entity = queryFactory
				.selectFrom(QMemberEntity.memberEntity)
				.where(	QMemberEntity.memberEntity.username.eq(request.getUsername()),
						QMemberEntity.memberEntity.state.eq(State.ACTIVE))
				.fetchOne();
		
		if (entity != null) {

			if (encoder.matches(request.getPassword(), entity.getPassword())) {
				
				return entityMapper.toResponse(entity);
				
			} else {
				
				throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_MATCHED_PASSWORD);
			}
		} else {
			
			throw new RestServiceException(HttpStatus.GONE, ErrorCode.NOT_FOUND_MEMBER);
		}
	}

}
