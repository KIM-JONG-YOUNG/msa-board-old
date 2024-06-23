package com.jong.msa.board.core.web.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.core.web.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(ErrorResponseHandler.ORDER)
public class ErrorResponseHandler extends ResponseEntityExceptionHandler {

	public static final int ORDER = Ordered.LOWEST_PRECEDENCE;
	
	@ExceptionHandler(RestServiceException.class)
	ResponseEntity<ErrorResponse> handleRestServiceException(RestServiceException e) {

		return ResponseEntity.status(e.getStatus())
				.body(ErrorResponse.builder()
						.errorCode(e.getErrorCode().getCode())
						.errorMessage(e.getErrorCode().getMessage())
						.errorDetailsList(e.getErrorDetailsList())
						.build());
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> handleException(Exception e) {

		log.error(e.getMessage(), e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorResponse.builder()
						.errorCode(ErrorCode.UNCHECKED_INTERNAL_ERROR.name())
						.errorMessage(ErrorCode.UNCHECKED_INTERNAL_ERROR.getMessage())
						.build());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		log.error(ex.getMessage(), ex);

		return ResponseEntity.status(status)
				.body(ErrorResponse.builder()
						.errorCode(ErrorCode.UNCHECKED_INTERNAL_ERROR.name())
						.errorMessage(ErrorCode.UNCHECKED_INTERNAL_ERROR.getMessage())
						.build());
	}

}
