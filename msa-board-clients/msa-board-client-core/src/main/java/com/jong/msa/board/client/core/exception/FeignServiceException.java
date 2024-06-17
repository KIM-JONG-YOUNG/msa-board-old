package com.jong.msa.board.client.core.exception;

import java.util.Collection;
import java.util.Map;

import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.FeignException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FeignServiceException extends FeignException {

	private static final long serialVersionUID = 1L;

	private final ErrorResponse body;

	public FeignServiceException(int status, Map<String, Collection<String>> headers, ErrorResponse errorResponse) {

		super(status, "Error Response = " + errorResponse, null, headers);
		this.body = errorResponse;
	}

}
