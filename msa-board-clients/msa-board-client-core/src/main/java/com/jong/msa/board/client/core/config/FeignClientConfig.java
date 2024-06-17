package com.jong.msa.board.client.core.config;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.client.core.exception.FeignServiceException;
import com.jong.msa.board.core.web.converter.LocalDateParamConverter;
import com.jong.msa.board.core.web.converter.LocalDateTimeParamConverter;
import com.jong.msa.board.core.web.converter.LocalTimeParamConverter;
import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.FeignException;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;

@Configuration
@EnableFeignClients(basePackages = "com.jong.msa.board.client.**.feign")
public class FeignClientConfig {

	@Bean
	Logger.Level feginLoggingLevel() {
		
		return Logger.Level.FULL;
	}

	@Bean
	FeignFormatterRegistrar localDateFeignFormatterRegister() {

		return registry -> {

			DateTimeFormatterRegistrar localDateTimeRegistrar = new DateTimeFormatterRegistrar();

			localDateTimeRegistrar.setTimeFormatter(LocalTimeParamConverter.FORMATTER);
			localDateTimeRegistrar.setDateFormatter(LocalDateParamConverter.FORMATTER);
			localDateTimeRegistrar.setDateTimeFormatter(LocalDateTimeParamConverter.FORMATTER);

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
					
					return new FeignServiceException(response.status(), response.headers(), errorResponse);

				} catch (Exception e) {
					
					return FeignException.errorStatus(methodKey, response);
				}
			}
		};
	}

}
