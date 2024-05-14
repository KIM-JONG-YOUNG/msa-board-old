package com.jong.msa.board.core.redis.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.jong.msa.board.core.redis.test.service.RedisCoreTestService;

@SpringBootTest
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class, 
		classes = RedisCoreTestContext.class)
public class RedisCoreTest {

	public static final String CACHING_KEY_PREFIX = "test::";
	public static final String CACHING_KEY_1 = "key-1";
	public static final String CACHING_KEY_2 = "key-2";
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@SpyBean
	RedisCoreTestService testService;
	 
	@Test
	void RedisCaching_어노테이션_테스트() {

		testService.caching(RedisCoreTest.CACHING_KEY_1);
		testService.caching(RedisCoreTest.CACHING_KEY_1); 
		
		verify(testService, times(1)).caching(any());
	}

	@Test
	void RedisRemove_어노테이션_테스트() {

		testService.caching(RedisCoreTest.CACHING_KEY_2);
		testService.remove(RedisCoreTest.CACHING_KEY_2);
	
		String cachingKey = new StringBuilder(RedisCoreTest.CACHING_KEY_PREFIX).append(RedisCoreTest.CACHING_KEY_2).toString();
		
		assertFalse(redisTemplate.hasKey(cachingKey));
	}

}
 