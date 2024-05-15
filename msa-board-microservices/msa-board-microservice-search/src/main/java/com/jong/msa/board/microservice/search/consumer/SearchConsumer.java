package com.jong.msa.board.microservice.search.consumer;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.constants.KafkaTopicNames;
import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.core.transaction.aspect.DistributeTransaction;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.repository.MemberRepository;
import com.jong.msa.board.domain.post.entity.PostEntity;
import com.jong.msa.board.domain.post.repository.PostRepository;
import com.jong.msa.board.microservice.search.mapper.SearchEntityMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchConsumer {

	private final MemberRepository memberRepository;

	private final PostRepository postRepository;

	private final SearchEntityMapper entityMapper;
	
	private final ObjectProvider<SearchConsumer> consumerProvider;

	private final ObjectMapper objectMapper;

	@KafkaListener(topics = KafkaTopicNames.MEMBER_SAVE_TOPIC)
	public void consumeMemberSaveTopic(String message) throws Exception {
		
		consumerProvider.getObject().save(objectMapper.readValue(message, MemberEntity.class));
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.MEMBER_LOCK_KEY, key = "#entity.id")
	public void save(MemberEntity entity) {
	
		try {

			MemberEntity dbEntity = memberRepository.findById(entity.getId()).orElseThrow(EntityNotFoundException::new);
			memberRepository.save(entityMapper.updateEntity(entity, dbEntity).setAuditable(false));

		} catch (EntityNotFoundException e) {
			
			memberRepository.save(entity);
		}
	}
	
	@KafkaListener(topics = KafkaTopicNames.POST_SAVE_TOPIC)
	public void consumePostSaveTopic(String message) throws Exception {
		
		consumerProvider.getObject().save(objectMapper.readValue(message, PostEntity.class));
	}

	@DistributeTransaction(prefix = RedisKeyPrefixes.POST_LOCK_KEY, key = "#entity.id")
	public void save(PostEntity entity) {
	
		try {

			PostEntity dbEntity = postRepository.findById(entity.getId()).orElseThrow(EntityNotFoundException::new);
			postRepository.save(entityMapper.updateEntity(entity, dbEntity).setAuditable(false));

		} catch (EntityNotFoundException e) {

			postRepository.save(entity);
		}
	}
	
}
