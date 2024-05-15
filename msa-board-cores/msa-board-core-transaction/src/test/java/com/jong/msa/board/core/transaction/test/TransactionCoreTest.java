package com.jong.msa.board.core.transaction.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;

import com.jong.msa.board.core.transaction.test.service.TrasactionCoreTestService;

@SpringBootTest
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class,
		classes = TransactionCoreTestContext.class)
public class TransactionCoreTest {

	@Autowired
	TrasactionCoreTestService service;
	 
	@Test
	void DistributeTransaction_어노테이션_테스트() throws Exception {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.initialize();

		int loopCount = 100;
		
		CountDownLatch latch = new CountDownLatch(loopCount);
		
		UUID id = service.create();
		
		for (int i = 0; i < loopCount; i++) {
			
			executor.execute(() -> {
				service.increaseCount(id);
				latch.countDown();
			});
		}
		
		latch.await();
		
		assertEquals(loopCount, service.getCount(id));
	}

}
