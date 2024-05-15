package com.jong.msa.board.core.feign.exception;

import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.FeignException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FeignServiceException extends FeignException {

	private static final long serialVersionUID = 1L;

	private final ErrorResponse errorResponse;

	public FeignServiceException(int status, ErrorResponse errorResponse) {

		super(status, "Error Response = " + errorResponse);
		this.errorResponse = errorResponse;
	}
	
}
