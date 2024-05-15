package com.jong.msa.board.core.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCoreConfig {

	@Bean 
	RedisConnectionFactory redisConnectionFactory(
			@Value("${spring.redis.host}") String host, 
			@Value("${spring.redis.port}") int port) {
		
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
	}

	@Bean
	RedissonClient redissonClient(
			@Value("${spring.redis.host}") String host, 
			@Value("${spring.redis.port}") int port) {

		org.redisson.config.Config config = new Config();

		config.useSingleServer().setAddress(String.format("redis://%s:%s", host, port));
		
		return Redisson.create(config);
	}

	@Bean
	RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		
		return redisTemplate;
	}
	
}
