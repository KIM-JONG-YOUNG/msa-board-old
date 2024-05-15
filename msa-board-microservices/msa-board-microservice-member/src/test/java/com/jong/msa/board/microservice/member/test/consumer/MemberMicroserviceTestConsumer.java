package com.jong.msa.board.microservice.member.test.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jong.msa.board.common.constants.KafkaTopicNames;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MemberMicroserviceTestConsumer {

	@KafkaListener(topics = KafkaTopicNames.MEMBER_SAVE_TOPIC)
	public void consumeMemberSaveTopic(String message) {
		
		log.info("Kafka Consume Message => {}", message);
	} 
	
}
