package com.jong.msa.board.core.feign.config;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.IOUtils;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.constants.Patterns;
import com.jong.msa.board.core.feign.exception.FeignServiceException;
import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.FeignException;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;

@Configuration
@EnableFeignClients(basePackages = "com.jong.msa.board.**.client")
public class FeignCoreConfig {

	@Bean
	Logger.Level feginLoggingLevel() {
		
		return Logger.Level.FULL;
	}

	@Bean
	FeignFormatterRegistrar localDateFeignFormatterRegister() {

		return registry -> {

			DateTimeFormatterRegistrar localDateTimeRegistrar = new DateTimeFormatterRegistrar();

			localDateTimeRegistrar.setTimeFormatter(DateTimeFormatter.ofPattern(Patterns.TIME_FORMAT));
			localDateTimeRegistrar.setDateFormatter(DateTimeFormatter.ofPattern(Patterns.DATE_FORMAT));
			localDateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(Patterns.DATE_TIME_FORMAT));

			localDateTimeRegistrar.registerFormatters(registry);
		};
	}
	
	@Bean
	ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
		
		return new ErrorDecoder() {
			
			@Override
			public Exception decode(String methodKey, Response response) {

				try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {

					ErrorResponse errorResponse = objectMapper.readValue(IOUtils.toString(reader), ErrorResponse.class);
					
					return new FeignServiceException(response.status(), errorResponse);

				} catch (Exception e) {
					
					return FeignException.errorStatus(methodKey, response);
				}
			}
		};
	}

}
