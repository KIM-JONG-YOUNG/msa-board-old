package com.jong.msa.board.core.kafka.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaCoreConfig {

	private final KafkaProducerErrorHandler producerErrorHandler;

	private final KafkaConsumerErrorHandler consumerErrorHandler;

	@Bean
	ProducerFactory<String, String> producerFactory(KafkaProperties properties) {
		
		Map<String, Object> propMap = properties.buildProducerProperties();
		propMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);		// Producer exactly once 설정 
		propMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		propMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		return new DefaultKafkaProducerFactory<>(propMap);
	}

	@Bean
	ConsumerFactory<String, String> consumerFactory(KafkaProperties properties) {

		Map<String, Object> propMap = properties.buildConsumerProperties();
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
