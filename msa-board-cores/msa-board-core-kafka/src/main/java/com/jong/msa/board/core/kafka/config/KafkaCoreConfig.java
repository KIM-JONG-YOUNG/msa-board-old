package com.jong.msa.board.core.kafka.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

import com.jong.msa.board.core.kafka.handler.KafkaConsumerErrorHandler;
import com.jong.msa.board.core.kafka.handler.KafkaProducerErrorHandler;

import lombok.RequiredArgsConstructor;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaCoreConfig {

	private final KafkaProducerErrorHandler producerErrorHandler;

	private final KafkaConsumerErrorHandler consumerErrorHandler;

	@Bean
	ProducerFactory<String, String> producerFactory(
			@Value("${spring.kafka.producer.bootstrap-servers}") List<String> serverList) {

		Map<String, Object> propMap = new HashMap<>();
		
		propMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverList);	// Kafka Broker 서버 IP 및 포트
		
		propMap.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 60000);				// Kafka Broker 로부터 Metadata를 받기까지 최대 대기 시간 (60s) 
		propMap.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);		// Kafka Broker로 전송 실패 여부 판단을 위한 최대 대기 시간 (60s)
		propMap.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);		// Kafka Broker로 전송 후 Metadata를 받기 위한 최대 시간 (30s)
		propMap.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);			// 재시도 대기 시간 (1s)
		propMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);		// Producer exactly once 설정 
		propMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		propMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		return new DefaultKafkaProducerFactory<>(propMap);
	}

	@Bean
	ConsumerFactory<String, String> consumerFactory(
			@Value("${spring.kafka.consumer.group-id}") String groupId,
			@Value("${spring.kafka.consumer.bootstrap-servers}") List<String> serverList,
			@Value("${spring.kafka.consumer.auto-offset-reset}") String autoOffsetReset) {

		Map<String, Object> propMap = new HashMap<>();
		
		propMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverList);		// Kafka Broker 서버 IP 및 포트 
		propMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);	// Topic 관련 Metadata 정보가 없을 경우 처리 방식 
		propMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);					// Consumer 그룹 ID 

		propMap.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);	// 재시도 대기 시간 (1s)
		propMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		propMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(propMap);
	}

	@Bean
	KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {

		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
		kafkaTemplate.setProducerListener(producerErrorHandler);
		
		return kafkaTemplate;
	}
	
	@Bean
	ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setCommonErrorHandler(consumerErrorHandler);
		factory.getContainerProperties().setAckMode(AckMode.RECORD);	// Kafka 메시지를 레코드 단위로 처리하고 offset을 커밋 

		return factory;
	}

}
