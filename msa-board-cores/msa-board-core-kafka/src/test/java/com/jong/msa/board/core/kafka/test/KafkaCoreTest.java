package com.jong.msa.board.core.kafka.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import com.jong.msa.board.core.kafka.handler.KafkaConsumerErrorHandler;
import com.jong.msa.board.core.kafka.handler.KafkaProducerErrorHandler;
import com.jong.msa.board.core.kafka.test.consumer.KafkaCoreTestConsumer;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@EmbeddedKafka(
		ports = 9092, partitions = 1, 
		brokerProperties = { 
				"listeners=PLAINTEXT://localhost:9092",
				"auto.create.topics.enable=false"		})
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class, 
		classes = KafkaCoreTestContext.class)
public class KafkaCoreTest {

	public static final String TEST_TOPIC = "TEST-TOPIC";
	public static final String ERROR_TOPIC = "ERROR-TOPIC";
	public static final String NOT_EXISTS_TOPIC = "NOT-EXISTS-TOPIC";
	
	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;

	@SpyBean
	KafkaCoreTestConsumer testConsumer;

	@SpyBean
	KafkaProducerErrorHandler producerErrorHandler;

	@SpyBean
	KafkaConsumerErrorHandler consumerErrorHandler;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@BeforeAll
	public void Kafka_Topic_추가() {
		
		embeddedKafkaBroker.doWithAdmin(adminClient -> {
			
			adminClient.createTopics(Arrays.asList(TopicBuilder.name(KafkaCoreTest.TEST_TOPIC).build()));
			adminClient.createTopics(Arrays.asList(TopicBuilder.name(KafkaCoreTest.ERROR_TOPIC).build()));
		});
	}
	
	// 테스트 시간이 최소 60초 이상 소요 
	@Disabled
	@Test
	void 존재하지_않는_Topic_전송_오류_처리_테스트() {

		assertThrows(KafkaException.class, () -> kafkaTemplate.send("not-exists-topic", "존재하지 않는 토픽으로 전송하는 메시지"));
		
		verify(producerErrorHandler, timeout(5000)).onError(any(), any(), any());
	}

	@Test
	void 카프카_브로커에_1MB_이상의_메시지_전송_오류_테스트() {

		assertThrows(KafkaException.class, () -> {
			
			int messageSize = 1024 * 1024;	
			
			StringBuilder messageBuilder = new StringBuilder("1MB 이상의 메시지");

			IntStream.range(0, messageSize).forEach(i -> messageBuilder.append("A"));
			
			kafkaTemplate.send(KafkaCoreTest.TEST_TOPIC, messageBuilder.toString());
		});

		verify(producerErrorHandler, timeout(5000)).onError(any(), any(), any());
	}
	
	@Test
	void Consumer_오류가_발생할_경우_처리_테스트() {

		kafkaTemplate.send(KafkaCoreTest.ERROR_TOPIC, "컨슈머 오류 테스트 메시지");
		
		verify(consumerErrorHandler, timeout(5000)).handleRecord(any(), any(), any(), any());
	}

}
