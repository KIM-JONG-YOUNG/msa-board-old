package com.jong.msa.board.client.core.config;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.converter.LocalDateParamConverter;
import com.jong.msa.board.core.web.converter.LocalDateTimeParamConverter;
import com.jong.msa.board.core.web.converter.LocalTimeParamConverter;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

@Configuration
@EnableFeignClients(basePackages = "com.jong.msa.board.client.**.feign")
public class FeignClientConfig {

	@Bean
	feign.Logger.Level feginLoggingLevel() {
		
		return feign.Logger.Level.FULL;
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

			private final Logger log = LoggerFactory.getLogger(ErrorDecoder.class);
			
			@Override
			public Exception decode(String methodKey, Response response) {

				try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {

					ErrorResponse errorResponse = objectMapper.readValue(IOUtils.toString(reader), ErrorResponse.class);

					String errorResponseCode = errorResponse.getErrorCode();

					HttpStatus status = HttpStatus.valueOf(response.status());

					ErrorCode errorCode = EnumSet.allOf(ErrorCode.class).stream()
							.filter(x -> x.getCode().equals(errorResponseCode))
							.findAny().orElseThrow(() -> new EnumConstantNotPresentException(ErrorCode.class, errorResponseCode));

					return new RestServiceException(status , errorCode, errorResponse.getErrorDetailsList());

				} catch (Exception e) {
					
					log.error("외부 시스템을 호출하는데 오류가 발생했습니다.", FeignException.errorStatus(methodKey, response));
					
					return new RestServiceException(HttpStatus.BAD_GATEWAY , ErrorCode.UNCHECKED_EXTERNAL_ERROR);
				}
			}
		};
	}

}
