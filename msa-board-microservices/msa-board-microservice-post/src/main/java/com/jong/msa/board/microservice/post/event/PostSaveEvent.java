package com.jong.msa.board.microservice.post.event;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.constants.KafkaTopicNames;
import com.jong.msa.board.domain.post.entity.PostEntity;
import com.jong.msa.board.domain.post.repository.PostRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class PostSaveEvent {

	private final UUID id;

	@Component
	@RequiredArgsConstructor
	public static class Listener {
		
		private final PostRepository repository;
		
		private final ObjectMapper objectMapper;
		
		private final KafkaTemplate<String, String> kafkaTemplate;

		@TransactionalEventListener
		@Transactional(readOnly = true)
		public void listen(PostSaveEvent event) throws Exception {
			
			PostEntity entity = repository.findById(event.getId()).orElseThrow(EntityNotFoundException::new);
			
			kafkaTemplate.send(KafkaTopicNames.POST_SAVE_TOPIC, objectMapper.writeValueAsString(entity));
		}
		
	}
	
}
