package com.jong.msa.board.core.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.security.exception.RevokedJwtException;
import com.jong.msa.board.core.web.response.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationErrorFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	private void sendError(HttpServletResponse response, HttpStatus status, ErrorCode errorCode) throws ServletException, IOException {
		
		response.setStatus(status.value());
		response.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.builder()
				.errorCode(errorCode.getCode())
				.errorMessage(errorCode.getMessage())
				.build()));
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {
			
			sendError(response, HttpStatus.UNAUTHORIZED, ErrorCode.EXPIRED_ACCESS_TOKEN);
			
		} catch (RevokedJwtException e) {

			sendError(response, HttpStatus.UNAUTHORIZED, ErrorCode.REVOKED_ACCESS_TOKEN);
			
		} catch (JwtException e) {

			sendError(response, HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_ACCESS_TOKEN);
		}
	}

}
