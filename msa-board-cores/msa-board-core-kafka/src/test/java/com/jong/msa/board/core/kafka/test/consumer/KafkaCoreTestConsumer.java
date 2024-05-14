package com.jong.msa.board.core.kafka.test.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jong.msa.board.core.kafka.test.KafkaCoreTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaCoreTestConsumer {

	@KafkaListener(topics = KafkaCoreTest.TEST_TOPIC)
	public void consumeTestTopic(String message) {
		
		log.info("Kafka Consume Message : {}", message);
	}

	@KafkaListener(topics = KafkaCoreTest.ERROR_TOPIC)
	public void consumeErrorTopic(String message) {
		
		log.info("Kafka Consume Message : {}", message);
		
		throw new RuntimeException("Kafka Consumer Message!!!");
	}
	
}
