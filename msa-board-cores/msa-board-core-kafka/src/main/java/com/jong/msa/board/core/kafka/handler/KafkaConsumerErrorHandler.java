package com.jong.msa.board.core.kafka.handler;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaConsumerErrorHandler implements CommonErrorHandler {

	@Override
	public void handleRecord(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer,
			MessageListenerContainer container) {

		log.error(new StringBuilder("Kafka Consumer Error!!!")
				.append("\n - Consumer	: ").append(consumer.groupMetadata().groupId())
				.append("\n - Topic   	: ").append(record.topic())
				.append("\n - Message	: ").append(record.value())
				.toString(), thrownException);
	}
	
}
