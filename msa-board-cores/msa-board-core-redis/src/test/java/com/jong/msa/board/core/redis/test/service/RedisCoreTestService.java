package com.jong.msa.board.core.redis.test.service;

import org.springframework.stereotype.Component;

import com.jong.msa.board.core.redis.aspect.RedisCaching;
import com.jong.msa.board.core.redis.aspect.RedisRemove;
import com.jong.msa.board.core.redis.test.RedisCoreTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisCoreTestService {

	@RedisCaching(prefix = RedisCoreTest.CACHING_KEY_PREFIX, key = "#key")
	public String caching(String key) {
	
		log.info("caching data");
		
		return "Message";
	}

	@RedisRemove(prefix = RedisCoreTest.CACHING_KEY_PREFIX, key = "#key")
	public void remove(String key) {

		log.info("remove data");
	}

}
