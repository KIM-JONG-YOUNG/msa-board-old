package com.jong.msa.board.core.web.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.jong.msa.board.core.web.enums.ErrorCodeEnum;
import com.jong.msa.board.core.web.response.ErrorResponse;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RestServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
	
	private final ErrorCodeEnum errorCode;

	private final List<ErrorResponse.Details> errorDetailsList;

	public RestServiceException(HttpStatus status, ErrorCodeEnum errorCode, List<ErrorResponse.Details> errorDetailsList) {

		super(errorCode.getMessage());
		this.status = status;
		this.errorCode = errorCode;
		this.errorDetailsList = errorDetailsList;
	}

	public RestServiceException(HttpStatus status, ErrorCodeEnum errorCode) {
		
		this(status, errorCode, new ArrayList<>());
	}

	public RestServiceException(HttpStatus status, ErrorCodeEnum errorCode, BindingResult bindingResult) {

		this(status, errorCode);
		
		this.errorDetailsList.addAll(bindingResult.getGlobalErrors().stream()
				.map(x -> ErrorResponse.Details.builder()
						.message(x.getDefaultMessage())
						.build())
				.collect(Collectors.toList()));
		
		this.errorDetailsList.addAll(bindingResult.getFieldErrors().stream()
				.map(x -> ErrorResponse.Details.builder()
						.field(x.getField())
						.message(x.getDefaultMessage())
						.build())
				.collect(Collectors.toList()));
	}

}
