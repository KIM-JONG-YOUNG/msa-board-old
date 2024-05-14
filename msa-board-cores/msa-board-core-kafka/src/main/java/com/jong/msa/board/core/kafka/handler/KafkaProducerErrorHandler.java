package com.jong.msa.board.core.kafka.handler;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaProducerErrorHandler implements ProducerListener<String, String> {

	@Override
	public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata,
			Exception exception) {

		log.error(new StringBuilder("Kafka Producer Error!!!")
				.append("\n - Topic   	: ").append(producerRecord.topic())
				.append("\n - Message	: ").append(producerRecord.value())
				.toString(), exception);
	}
	
}
