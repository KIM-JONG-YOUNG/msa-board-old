package com.jong.msa.board.core.web.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.dto.ErrorDetails;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RestServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
	
	private final ErrorCode errorCode;

	private final List<ErrorDetails> errorDetailsList;

	public RestServiceException(HttpStatus status, ErrorCode errorCode, List<ErrorDetails> errorDetailsList) {

		super(errorCode.getMessage());
		this.status = status;
		this.errorCode = errorCode;
		this.errorDetailsList = errorDetailsList;
	}

	public RestServiceException(HttpStatus status, ErrorCode errorCode) {
		
		this(status, errorCode, new ArrayList<>());
	}

}
