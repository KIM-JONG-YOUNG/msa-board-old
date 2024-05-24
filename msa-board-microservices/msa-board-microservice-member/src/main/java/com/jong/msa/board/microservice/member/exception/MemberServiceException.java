package com.jong.msa.board.microservice.member.exception;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.CodeEnum.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;

public class MemberServiceException extends RestServiceException {

	private static final long serialVersionUID = 1L;

	protected MemberServiceException(HttpStatus status, ErrorCode errorCode) {
		super(status, errorCode);
	}

	public static MemberServiceException notFoundMember() {
		
		return new MemberServiceException(HttpStatus.GONE, ErrorCode.NOT_FOUND_MEMBER);
	}

	public static MemberServiceException notFoundMemberByUsername() {
		
		return new MemberServiceException(HttpStatus.GONE, ErrorCode.NOT_FOUND_MEMBER_USERNAME);
	}

	public static MemberServiceException duplicateMemberUsername() {
		
		return new MemberServiceException(HttpStatus.CONFLICT, ErrorCode.DUPLICATED_MEMBER_USERNAME);
	}

	public static MemberServiceException notMatchedMemberPassword() {
		
		return new MemberServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_MATCHED_MEMBER_PASSWORD);
	}

}
