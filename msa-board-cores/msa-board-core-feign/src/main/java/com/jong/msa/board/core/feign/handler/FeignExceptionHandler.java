package com.jong.msa.board.core.feign.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jong.msa.board.common.enums.ErrorCodeEnum.CommonErrorCode;
import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE + 1)	// ErrorResponseHandler 보다 먼저 실행 
@RestControllerAdvice
public class FeignExceptionHandler {

	@ExceptionHandler(FeignException.class)
	ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {

		log.error(e.getMessage(), e);

		CommonErrorCode errorCode = CommonErrorCode.UNCHECKED_ERROR;
		
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
				.body(ErrorResponse.builder()
						.errorCode(errorCode.getCode())
						.errorMessage(errorCode.getMessage())
						.build());
	}
	
}
