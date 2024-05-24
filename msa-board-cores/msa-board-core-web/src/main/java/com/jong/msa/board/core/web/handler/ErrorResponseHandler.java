package com.jong.msa.board.core.web.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jong.msa.board.common.enums.CodeEnum.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.core.web.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class ErrorResponseHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RestServiceException.class)
	ResponseEntity<Object> handleRestServiceException(RestServiceException e) {

		ErrorCode errorCode = e.getErrorCode();
		
		List<ErrorResponse.Details> errorList = e.getErrorList().stream()
				.map(error -> (error instanceof FieldError) 
						? ErrorResponse.Details.builder()
								.field(((FieldError) error).getField())
								.message(error.getDefaultMessage())
								.build()
						: ErrorResponse.Details.builder()
								.message(error.getDefaultMessage())
								.build())
				.collect(Collectors.toList());
		
		return ResponseEntity.status(e.getStatus())
				.body(ErrorResponse.builder()
						.errorCode(errorCode.getCode())
						.errorMessage(errorCode.getMessage())
						.errorDetailsList(errorList)
						.build());
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<Object> handleException(Exception e) {

		log.error(e.getMessage(), e);

		return handleRestServiceException(RestServiceException.uncheckedError(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		log.error(ex.getMessage(), ex);

		return handleRestServiceException(RestServiceException.uncheckedError(status));
	}

}
