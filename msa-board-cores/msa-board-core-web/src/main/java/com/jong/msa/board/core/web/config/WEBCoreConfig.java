package com.jong.msa.board.core.web.config;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.jong.msa.board.common.constants.Patterns;

@Configuration
@ServletComponentScan(basePackages = "com.jong.msa.board.**.filter")
public class WEBCoreConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")     	// 모든 요청
			.allowedOriginPatterns("*")	// 모든 요청 Origin 허용 
			.allowedMethods("*")       	// 모든 요청 메소드 허용 
			.allowedHeaders("*")       	// 모든 요청 헤더 허용 
			.exposedHeaders("*");      	// 모든 응답 헤더 허용 
	}

	@Bean
	Jackson2ObjectMapperBuilderCustomizer customizer() {

		// objectMapper 날짜 변환 형식 지정 
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Patterns.TIME_FORMAT);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Patterns.DATE_FORMAT);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Patterns.DATE_TIME_FORMAT);
		
		return builder-> builder
				.featuresToDisable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)	// 기본 자료형에 null 값으로 매핑되어도 오류를 발생시키지 않음 
				.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)	// 매핑되지 않은 필드가 존재 시 오류를 발생시키지 않음 
				.serializationInclusion(JsonInclude.Include.NON_NULL)					// Null 값 필드는 json으로 변환하지 않음  
				.serializationInclusion(JsonInclude.Include.NON_EMPTY)					// 비어있는 값 필드는 json으로 변환하지 않음 
				.modules(new JavaTimeModule())
				.serializers(new LocalTimeSerializer(timeFormatter))
		        .serializers(new LocalDateSerializer(dateFormatter))
		        .serializers(new LocalDateTimeSerializer(dateTimeFormatter))
		        .deserializers(new LocalTimeDeserializer(timeFormatter))
		        .deserializers(new LocalDateDeserializer(dateFormatter))
		        .deserializers(new LocalDateTimeDeserializer(dateTimeFormatter))
				.build();
	}

}
