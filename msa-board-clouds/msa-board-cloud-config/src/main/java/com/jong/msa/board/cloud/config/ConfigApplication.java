package com.jong.msa.board.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableConfigServer
@SpringBootApplication
public class ConfigApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(ConfigApplication.class, args);
		
		Environment environment = context.getEnvironment();
		 
		String configDirPath = environment.getProperty("spring.cloud.config.server.native.search-locations");

		configDirPath = (configDirPath.startsWith("file:")) ? configDirPath.replace("file:", "") : configDirPath;
		
		log.info("Config Directory Path : {}", configDirPath );
	}
	
}
