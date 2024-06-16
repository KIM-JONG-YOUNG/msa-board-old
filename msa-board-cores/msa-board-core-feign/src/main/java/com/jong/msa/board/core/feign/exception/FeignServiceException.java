package com.jong.msa.board.core.feign.exception;

import org.springframework.http.HttpHeaders;

import com.jong.msa.board.core.web.response.ErrorResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class FeignServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int statusCode;
	
	private final HttpHeaders headers;
	
	private final ErrorResponse body;

//	public FeignServiceException(int status, ErrorResponse errorResponse) {
//
//		super(status, "Error Response = " + errorResponse);
//		this.errorResponse = errorResponse;
//	}
//
//	public FeignServiceException(int status, Map<String, Collection<String>> headers, ErrorResponse errorResponse) {
//
//		super(status, null, null, headers);
//		this.errorResponse = errorResponse;
//	}

}
