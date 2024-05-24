package com.jong.msa.board.microservice.post.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.CodeEnum.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class PostServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected PostServiceException(HttpStatus status, ErrorCode errorCode) {
		super(status, errorCode);
	}

	public static PostServiceException notFoundPost() {
		
		return new PostServiceException(HttpStatus.GONE, ErrorCode.NOT_FOUND_POST);
	}

	public static PostServiceException notFoundPostWriter() {
		
		return new PostServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_POST_WRITER);
	}
	
}
