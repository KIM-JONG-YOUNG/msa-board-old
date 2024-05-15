package com.jong.msa.board.core.persist.config;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EntityScan(basePackages = {
		"com.jong.msa.board.**.entity", 
		"com.jong.msa.board.**.converter"	})
@EnableJpaRepositories(basePackages = "com.jong.msa.board.**.repository")
public class PersistCoreConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	HikariConfig hikariConfig() {
		
		return new HikariConfig();
	}

	@Bean
	DataSource dataSource(HikariConfig hikariConfig) {
		
		return new HikariDataSource(hikariConfig);
	}

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		
		return new JPAQueryFactory(entityManager);
	}

}
