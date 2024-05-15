package com.jong.msa.board.core.transaction.test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.util.Architecture;
import redis.embedded.util.OS;

@Configuration
@AutoConfigureDataJpa
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.jong.msa.board")
public class TransactionCoreTestContext {

	@Value("${spring.redis.port}") 
	int redisPort;

	RedisServer embeddedRedisServer;

	@Bean
	ObjectMapper objectMapper() {

		return new ObjectMapper();
	}

	@PostConstruct
	public void start() throws Exception {

		(embeddedRedisServer = RedisServer.builder()
				.redisExecProvider(RedisExecProvider.defaultProvider()
						.override(OS.MAC_OS_X, Architecture.x86_64, "binary/redis-server-6.2.5-mac-arm64")
						.override(OS.MAC_OS_X, Architecture.x86, "binary/redis-server-6.2.5-mac-arm64"))
				.port(redisPort)
				.build()).start();
	}

	@PreDestroy
	public void end() throws Exception {
		
		if (embeddedRedisServer != null && embeddedRedisServer.isActive()) {
			embeddedRedisServer.stop();
		}
	}
	
}
