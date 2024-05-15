package com.jong.msa.board.endpoint.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCodeEnum;
import com.jong.msa.board.common.enums.ErrorCodeEnum.TokenErrorCode;
import com.jong.msa.board.common.enums.ErrorCodeEnum.UserErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class UserServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected UserServiceException(HttpStatus status, ErrorCodeEnum errorCode) {
		super(status, errorCode);
	}

	public static UserServiceException notUserGroupUsername() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, UserErrorCode.NOT_USER_GROUP_USERNAME);
	}
	
	public static UserServiceException notPostWriter() {
		
		return new UserServiceException(HttpStatus.FORBIDDEN, UserErrorCode.NOT_POST_WRITER);
	}

	public static UserServiceException expiredRefreshToken() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, TokenErrorCode.EXPIRED_REFRESH_TOKEN);
	}

	public static UserServiceException revokeRefreshToken() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, TokenErrorCode.REVOKED_REFRESH_TOKEN);
	}

	public static UserServiceException invalidRefreshToken() {
		
		return new UserServiceException(HttpStatus.BAD_REQUEST, TokenErrorCode.INVALID_REFRESH_TOKEN);
	}

}
