package com.jong.msa.board.core.security.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.handler.ErrorResponseHandler;
import com.jong.msa.board.core.web.response.ErrorResponse;

@RestControllerAdvice
@Order(AccessDeniedExceptionHandler.ORDER)
public class AccessDeniedExceptionHandler {

	public static final int ORDER = ErrorResponseHandler.ORDER + 1;

	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ErrorResponse.builder()
						.errorCode(ErrorCode.NOT_ACCESSIBLE_URL.name())
						.errorMessage(ErrorCode.NOT_ACCESSIBLE_URL.getMessage())
						.build());
	}

}
