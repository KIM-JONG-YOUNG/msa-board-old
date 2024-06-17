package com.jong.msa.board.client.core.utils;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jong.msa.board.client.core.exception.FeignServiceException;
import com.jong.msa.board.core.web.response.ErrorResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public final class FeignExcuteUtils {

	public static <T> Response<T> excute(Supplier<ResponseEntity<T>> supplier) {
		
		try {
			
			ResponseEntity<T> response = supplier.get();
			
			HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

			return Response.<T>builder()
					.status(status)
					.headers(response.getHeaders())
					.body(response.getBody())
					.build();
			
		} catch (FeignServiceException e) {

			HttpStatus status = HttpStatus.valueOf(e.status());
			
			HttpHeaders headers = e.responseHeaders().entrySet().stream()
					.collect(Collectors.toMap(
							x -> x.getKey(), 
							x -> x.getValue().stream()
								.collect(Collectors.toList()),
							(oldValue, newValue) -> Stream.concat(
									oldValue.stream(), 
									newValue.stream())
								.collect(Collectors.toList()),
							HttpHeaders::new));
			
			return Response.<T>builder()
					.status(status)
					.headers(headers)
					.error(e.getBody())
					.build();
		}
	}
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response<T> {

		private HttpStatus status;
		
		private HttpHeaders headers;

		private T body;
		
		private ErrorResponse error;
		
	}
	
}
