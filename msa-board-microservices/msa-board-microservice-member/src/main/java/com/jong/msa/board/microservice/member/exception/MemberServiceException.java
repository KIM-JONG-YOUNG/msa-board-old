package com.jong.msa.board.microservice.member.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCodeEnum;
import com.jong.msa.board.common.enums.ErrorCodeEnum.MemberErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class MemberServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected MemberServiceException(HttpStatus status, ErrorCodeEnum errorCode) {
		super(status, errorCode);
	}

	public static MemberServiceException notFoundMember() {
		
		return new MemberServiceException(HttpStatus.GONE, MemberErrorCode.NOT_FOUND_MEMBER);
	}

	public static MemberServiceException notFoundMemberByUsername() {
		
		return new MemberServiceException(HttpStatus.GONE, MemberErrorCode.NOT_FOUND_MEMBER_USERNAME);
	}

	public static MemberServiceException duplicateMemberUsername() {
		
		return new MemberServiceException(HttpStatus.CONFLICT, MemberErrorCode.DUPLICATE_MEMBER_USERNAME);
	}

	public static MemberServiceException notMatchedMemberPassword() {
		
		return new MemberServiceException(HttpStatus.BAD_REQUEST, MemberErrorCode.INVALID_MEMBER_PASSWORD);
	}

}
