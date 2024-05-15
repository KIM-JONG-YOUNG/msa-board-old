	package com.jong.msa.board.microservice.member.event;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.constants.KafkaTopicNames;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.repository.MemberRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MemberSaveEvent {

	private final UUID id;

	@Component
	@RequiredArgsConstructor
	public static class Listener {
		
		private final MemberRepository repository;
		
		private final ObjectMapper objectMapper;
		
		private final KafkaTemplate<String, String> kafkaTemplate;

		@TransactionalEventListener
		@Transactional(readOnly = true)
		public void listen(MemberSaveEvent event) throws Exception {
			
			MemberEntity entity = repository.findById(event.getId()).orElseThrow(EntityNotFoundException::new);
			
			kafkaTemplate.send(KafkaTopicNames.MEMBER_SAVE_TOPIC, objectMapper.writeValueAsString(entity));
		}
		
	}
	
}
