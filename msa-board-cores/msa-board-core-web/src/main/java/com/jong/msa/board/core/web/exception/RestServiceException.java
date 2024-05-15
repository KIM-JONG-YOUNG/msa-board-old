package com.jong.msa.board.core.web.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.jong.msa.board.common.enums.ErrorCodeEnum;
import com.jong.msa.board.common.enums.ErrorCodeEnum.CommonErrorCode;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RestServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
	
	private final ErrorCodeEnum errorCode;

	private final List<ObjectError> errorList;

	protected RestServiceException(HttpStatus status, ErrorCodeEnum errorCode, List<ObjectError> errorList) {
		super(errorCode.getMessage());
		this.status = status;
		this.errorCode = errorCode;
		this.errorList = errorList;
	}

	protected RestServiceException(HttpStatus status, ErrorCodeEnum errorCode) {
		this(status, errorCode, new ArrayList<>());
	}

	public static RestServiceException invalidParameter(BindingResult bindingResult) {
		
		return new RestServiceException(HttpStatus.BAD_REQUEST, CommonErrorCode.INVALID_PARAMETER, bindingResult.getAllErrors());
	}

	public static RestServiceException uncheckedError(HttpStatus status) {
		
		return new RestServiceException(status, CommonErrorCode.UNCHECKED_ERROR);
	}

}
