package com.jong.msa.board.microservice.post.test.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jong.msa.board.common.constants.KafkaTopicNames;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PostMicroserviceTestConsumer {

	@KafkaListener(topics = KafkaTopicNames.POST_SAVE_TOPIC)
	public void consumePostSaveTopic(String message) {
		
		log.info("Kafka Consume Message => {}", message);
	} 
	
}
