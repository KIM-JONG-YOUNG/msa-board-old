package com.jong.msa.board.core.feign.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jong.msa.board.core.feign.enums.FeignErrorCode;
import com.jong.msa.board.core.web.handler.ErrorResponseHandler;
import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(FeignExceptionHandler.ORDER)	
public class FeignExceptionHandler {

	public static final int ORDER = ErrorResponseHandler.ORDER + 1;

	@ExceptionHandler(FeignException.class)
	ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {

		log.error(e.getMessage(), e);

		return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
				.body(ErrorResponse.builder()
						.errorCode(FeignErrorCode.UNCHECKED_GATEWAY_ERROR.getCode())
						.errorMessage(FeignErrorCode.UNCHECKED_GATEWAY_ERROR.getMessage())
						.build());
	}
	
}
