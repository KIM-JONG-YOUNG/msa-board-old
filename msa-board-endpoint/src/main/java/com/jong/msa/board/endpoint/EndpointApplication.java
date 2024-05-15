package com.jong.msa.board.endpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.jong.msa.board")
@EnableConfigurationProperties(EndpointApplication.TokenProperties.class)
public class EndpointApplication {

	public static void main(String[] args) { 
		
		SpringApplication.run(EndpointApplication.class, args);
	}
	
	@Getter
	@ToString
	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("jwt")
	public static class TokenProperties {

		private final Details accessToken;
		
		private final Details refreshToken;

		@Getter
		@ToString
		@ConstructorBinding
		@RequiredArgsConstructor
		public static class Details {

			private final String secretKey;
			
			private final long expireSeconds;
			
		}
	}
	
}
