package com.jong.msa.board.endpoint.user.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.CodeEnum.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class UserServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected UserServiceException(HttpStatus status, ErrorCode errorCode) {
		super(status, errorCode);
	}

	public static UserServiceException notUserGroupUsername() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_USER_GROUP_MEMBER_USERNAME);
	}
	
	public static UserServiceException notPostWriter() {
		
		return new UserServiceException(HttpStatus.FORBIDDEN, ErrorCode.NOT_POST_WRITER);
	}
	
	public static UserServiceException deactivePost() {
		
		return new UserServiceException(HttpStatus.GONE, ErrorCode.DEACTIVE_POST);
	}

	public static UserServiceException notUserGroupRefreshToken() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_USER_GROUP_REFRESH_TOKEN);
	}

	public static UserServiceException expiredRefreshToken() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, ErrorCode.EXPIRED_REFRESH_TOKEN);
	}

	public static UserServiceException revokeRefreshToken() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, ErrorCode.REVOKED_REFRESH_TOKEN);
	}

	public static UserServiceException invalidRefreshToken() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REFRESH_TOKEN);
	}

}
