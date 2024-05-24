package com.jong.msa.board.endpoint.admin.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.CodeEnum.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class AdminServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected AdminServiceException(HttpStatus status, ErrorCode errorCode) {
		super(status, errorCode);
	}

	public static AdminServiceException notAdminGroupUsername() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_ADMIN_GROUP_MEMBER_USERNAME);
	}

	public static AdminServiceException notAdminGroupPost() {
		
		return new AdminServiceException(HttpStatus.FORBIDDEN, ErrorCode.NOT_ADMIN_GROUP_POST);
	}

	public static AdminServiceException notUserGroupMember() {
		
		return new AdminServiceException(HttpStatus.FORBIDDEN, ErrorCode.NOT_USER_GROUP_MEMBER);
	}

	public static AdminServiceException notAdminGroupRefreshToken() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_ADMIN_GROUP_REFRESH_TOKEN);
	}

	public static AdminServiceException expiredRefreshToken() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, ErrorCode.EXPIRED_REFRESH_TOKEN);
	}

	public static AdminServiceException revokeRefreshToken() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, ErrorCode.REVOKED_REFRESH_TOKEN);
	}

	public static AdminServiceException invalidRefreshToken() {
		
		return new AdminServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REFRESH_TOKEN);
	}

}
