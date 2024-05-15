package com.jong.msa.board.microservice.member.test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.util.Architecture;
import redis.embedded.util.OS;

@Configuration
public class MemberMicroserviceTestContext {

	@Value("${spring.redis.port}") 
	int redisPort;

	RedisServer embeddedRedisServer;

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
