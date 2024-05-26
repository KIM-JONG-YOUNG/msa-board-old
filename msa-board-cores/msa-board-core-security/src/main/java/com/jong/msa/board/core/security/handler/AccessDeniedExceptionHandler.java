package com.jong.msa.board.core.security.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jong.msa.board.common.enums.CodeEnum.ErrorCode;
import com.jong.msa.board.core.web.response.ErrorResponse;

@Order(Ordered.LOWEST_PRECEDENCE + 1)
@RestControllerAdvice
public class AccessDeniedExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {

		HttpStatus status = HttpStatus.UNAUTHORIZED;
		ErrorCode errorCode = (ErrorCode) request.getAttribute("tokenErrorCode");

		if (errorCode == null) {
			status = HttpStatus.FORBIDDEN;
			errorCode = ErrorCode.INACCESSIBLE_URL;
		}

		return ResponseEntity.status(status)
				.body(ErrorResponse.builder()
						.errorCode(errorCode.getCode())
						.errorMessage(errorCode.getMessage())
						.build());
	}

}
