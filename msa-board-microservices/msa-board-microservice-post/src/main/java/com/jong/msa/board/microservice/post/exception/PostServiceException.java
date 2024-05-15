package com.jong.msa.board.microservice.post.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCodeEnum;
import com.jong.msa.board.common.enums.ErrorCodeEnum.PostErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class PostServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected PostServiceException(HttpStatus status, ErrorCodeEnum errorCode) {
		super(status, errorCode);
	}

	public static PostServiceException notFoundPost() {
		
		return new PostServiceException(HttpStatus.GONE, PostErrorCode.NOT_FOUND_POST);
	}

	public static PostServiceException notFoundPostWriter() {
		
		return new PostServiceException(HttpStatus.BAD_REQUEST, PostErrorCode.NOT_FOUND_POST_WRITER);
	}
	
}
