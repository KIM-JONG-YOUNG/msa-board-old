package com.jong.msa.board.core.feign.utils;

import java.util.function.Supplier;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.jong.msa.board.core.feign.exception.FeignServiceException;
import com.jong.msa.board.core.web.response.ErrorResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public final class FeignClientCaller {

	public static <T> FeignClientResponse<T> call(Supplier<ResponseEntity<T>> supplier) {
		
		try {
			
			ResponseEntity<T> response = supplier.get();
			
			return FeignClientResponse.<T>builder()
					.statusCode(response.getStatusCode().value())
					.headers(response.getHeaders())
					.body(response.getBody())
					.build();
			
		} catch (FeignServiceException e) {
			
			return FeignClientResponse.<T>builder()
					.statusCode(e.getStatusCode())
					.headers(e.getHeaders())
					.error(e.getBody())
					.build();
		}
	}
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FeignClientResponse<T> {

		private int statusCode;
		
		private HttpHeaders headers;

		private T body;
		
		private ErrorResponse error;
		
	}
	
}
