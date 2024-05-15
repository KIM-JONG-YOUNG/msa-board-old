package com.jong.msa.board.microservice.member.service;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.DBCodeEnum.State;
import com.jong.msa.board.core.redis.aspect.RedisCaching;
import com.jong.msa.board.core.redis.aspect.RedisRemove;
import com.jong.msa.board.core.transaction.aspect.DistributeTransaction;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.entity.QMemberEntity;
import com.jong.msa.board.domain.member.repository.MemberRepository;
import com.jong.msa.board.microservice.member.event.MemberSaveEvent;
import com.jong.msa.board.microservice.member.exception.MemberServiceException;
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
	public UUID createMember(CreateMemberRequest request) {
		
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
			throw MemberServiceException.duplicateMemberUsername();
		}
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.MEMBER_LOCK_KEY, key = "#id")
	@RedisRemove(prefix = RedisKeyPrefixes.MEMBER_KEY, key = "#id")
	@Override
	public void modifyMember(UUID id, ModifyMemberRequest request) {
		
		MemberEntity entity = repository.findById(id).orElseThrow(MemberServiceException::notFoundMember);
		MemberEntity savedEntity = repository.save(entityMapper.updateEntity(request, entity));
		
		eventPublisher.publishEvent(new MemberSaveEvent(savedEntity.getId()));
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.MEMBER_LOCK_KEY, key = "#id")
	@Override
	public void modifyMemberPassword(UUID id, ModifyMemberPasswordRequest request) {
		
		MemberEntity entity = repository.findById(id).orElseThrow(MemberServiceException::notFoundMember);
		
		if (encoder.matches(request.getCurrentPassword(), entity.getPassword())) {

			String encodedPassword = encoder.encode(request.getNewPassword());

			MemberEntity savedEntity = repository.save(entity.setPassword(encodedPassword));

			eventPublisher.publishEvent(new MemberSaveEvent(savedEntity.getId()));

		} else {
			throw MemberServiceException.notMatchedMemberPassword();
		}		
	}

	@Transactional(readOnly = true)
	@RedisCaching(prefix = RedisKeyPrefixes.MEMBER_KEY, key = "#id")
	@Override
	public MemberDetailsResponse getMember(UUID id) {
		
		return entityMapper.toResponse(repository.findById(id).orElseThrow(MemberServiceException::notFoundMember));
	}

	@Transactional(readOnly = true)
	@Override 
	public MemberDetailsResponse loginMember(LoginMemberRequest request) {
		
		MemberEntity entity = queryFactory
				.selectFrom(QMemberEntity.memberEntity)
				.where(	QMemberEntity.memberEntity.username.eq(request.getUsername()),
						QMemberEntity.memberEntity.state.eq(State.ACTIVE))
				.fetchOne();
		
		if (entity != null) {

			if (encoder.matches(request.getPassword(), entity.getPassword())) {
				return entityMapper.toResponse(entity);
			} else {
				throw MemberServiceException.notMatchedMemberPassword();
			}
		} else {
			throw MemberServiceException.notFoundMemberByUsername();
		}
	}

}
