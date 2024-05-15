package com.jong.msa.board.core.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableTransactionManagement
public class TransactionCoreConfig {

	@Bean
	TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {

		return new TransactionTemplate(transactionManager);
	}
	
}
