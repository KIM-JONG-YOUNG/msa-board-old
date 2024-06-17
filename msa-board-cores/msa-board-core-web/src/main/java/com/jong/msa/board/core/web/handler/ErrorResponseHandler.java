package com.jong.msa.board.core.web.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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

		Predicate<ObjectError> isFieldError = (error) -> error instanceof FieldError;
		Predicate<ObjectError> isNotFieldError = isFieldError.negate();

		ErrorCode errorCode = e.getErrorCode();

		List<ErrorResponse.Details> errorDetailsList = new ArrayList<>();
		
		errorDetailsList.addAll(e.getErrorList().stream()
				.filter(isNotFieldError)
				.map(x -> ErrorResponse.Details.builder()
						.message(x.getDefaultMessage())
						.build())
				.collect(Collectors.toList()));

		errorDetailsList.addAll(e.getErrorList().stream()
				.filter(isFieldError)
				.map(FieldError.class::cast)
				.map(x -> ErrorResponse.Details.builder()
						.field(x.getField())
						.message(x.getDefaultMessage())
						.build())
				.collect(Collectors.toList()));

		return ResponseEntity.status(errorCode.getStatusCode())
				.body(ErrorResponse.builder()
						.errorCode(errorCode)
						.errorDetailsList(errorDetailsList)
						.build());
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> handleException(Exception e) {

		log.error(e.getMessage(), e);

		ErrorCode errorCode = ErrorCode.UNCHECKED_INTERNAL_ERROR;
		
		return ResponseEntity.status(errorCode.getStatusCode())
				.body(ErrorResponse.builder()
						.errorCode(errorCode)
						.build());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		log.error(ex.getMessage(), ex);

		ErrorCode errorCode = ErrorCode.UNCHECKED_INTERNAL_ERROR;
		
		return ResponseEntity.status(errorCode.getStatusCode())
				.body(ErrorResponse.builder()
						.errorCode(errorCode)
						.build());
	}

}
