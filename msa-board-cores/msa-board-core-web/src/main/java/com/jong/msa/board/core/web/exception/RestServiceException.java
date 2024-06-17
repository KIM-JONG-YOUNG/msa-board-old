package com.jong.msa.board.core.web.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.ObjectError;

import com.jong.msa.board.common.enums.ErrorCode;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RestServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ErrorCode errorCode;

	private final List<ObjectError> errorList;

	public RestServiceException(ErrorCode errorCode, List<ObjectError> errorList) {

		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.errorList = errorList;
	}

	public RestServiceException(ErrorCode errorCode) {
		
		this(errorCode, new ArrayList<>());
	}

}
