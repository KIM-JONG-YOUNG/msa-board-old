package com.jong.msa.board.endpoint.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.jong.msa.board")
public class AdminApplication {

	public static void main(String[] args) { 
		
		SpringApplication.run(AdminApplication.class, args);
	}
	
}
