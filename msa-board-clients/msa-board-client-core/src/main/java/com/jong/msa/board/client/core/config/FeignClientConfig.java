package com.jong.msa.board.client.core.config;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;

import org.apache.commons.io.IOUtils;
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

import feign.Client;
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
	okhttp3.OkHttpClient okHttpClient() {
		
		return new okhttp3.OkHttpClient.Builder().build();
	}
	
	@Bean
	Client client(okhttp3.OkHttpClient okHttpClient) {
		
		return new feign.okhttp.OkHttpClient(okHttpClient);
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

					String errorResponseCode = errorResponse.getErrorCode();

					HttpStatus status = HttpStatus.valueOf(response.status());

					ErrorCode errorCode = EnumSet.allOf(ErrorCode.class).stream()
							.filter(x -> x.getCode().equals(errorResponseCode))
							.findAny().orElseThrow(() -> new EnumConstantNotPresentException(ErrorCode.class, errorResponseCode));

					return new RestServiceException(status , errorCode, errorResponse.getErrorDetailsList());

				} catch (Exception e) {
					
					return FeignException.errorStatus(methodKey, response);
				}
			}
		};
	}

}
