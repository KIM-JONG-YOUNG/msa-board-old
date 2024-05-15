package com.jong.msa.board.endpoint.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCodeEnum;
import com.jong.msa.board.common.enums.ErrorCodeEnum.AdminErrorCode;
import com.jong.msa.board.common.enums.ErrorCodeEnum.TokenErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class AdminServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected AdminServiceException(HttpStatus status, ErrorCodeEnum errorCode) {
		super(status, errorCode);
	}

	public static AdminServiceException notAdminGroupUsername() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, AdminErrorCode.NOT_ADMIN_GROUP_USERNAME);
	}

	public static AdminServiceException notAdminGroupPost() {
		
		return new AdminServiceException(HttpStatus.FORBIDDEN, AdminErrorCode.NOT_ADMIN_GROUP_POST);
	}

	public static AdminServiceException notUserGroupMember() {
		
		return new AdminServiceException(HttpStatus.FORBIDDEN, AdminErrorCode.NOT_USER_GROUP_MEMBER);
	}

	public static AdminServiceException expiredRefreshToken() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, TokenErrorCode.EXPIRED_REFRESH_TOKEN);
	}

	public static AdminServiceException revokeRefreshToken() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, TokenErrorCode.REVOKED_REFRESH_TOKEN);
	}

	public static AdminServiceException invalidRefreshToken() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, TokenErrorCode.INVALID_REFRESH_TOKEN);
	}

}
