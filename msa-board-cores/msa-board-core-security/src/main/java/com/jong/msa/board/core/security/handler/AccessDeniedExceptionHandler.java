package com.jong.msa.board.core.security.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jong.msa.board.common.enums.ErrorCodeEnum;
import com.jong.msa.board.common.enums.ErrorCodeEnum.SecurityErrorCode;
import com.jong.msa.board.common.enums.ErrorCodeEnum.TokenErrorCode;
import com.jong.msa.board.core.security.utils.SecurityContextUtils;
import com.jong.msa.board.core.web.response.ErrorResponse;

@Order(Ordered.LOWEST_PRECEDENCE + 1)
@RestControllerAdvice
public class AccessDeniedExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<ErrorResponse> handleAccessDeniedException(
			HttpServletRequest request, AccessDeniedException e) {

		boolean isAuthenticated = (SecurityContextUtils.getAuthenticationId() != null); 
		
		ErrorCodeEnum errorCode = null;

		if (isAuthenticated) {
			errorCode = SecurityErrorCode.NOT_ACCESSIBLE_MEMBER;
		} else {
			
			TokenErrorCode tokenErrorCode = (TokenErrorCode) request.getAttribute("tokenErrorCode");

			errorCode = (tokenErrorCode == null) ? SecurityErrorCode.UNAUTHORIZED_MEMBER : tokenErrorCode;  
		}
		
		return ResponseEntity.status((isAuthenticated) ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED)
				.body(ErrorResponse.builder()
						.errorCode(errorCode.getCode())
						.errorMessage(errorCode.getMessage())
						.build());
	}
	
}
