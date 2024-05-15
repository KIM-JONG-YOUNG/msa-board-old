package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * API 오류 코드 인터페이스
 */
public interface ErrorCodeEnum {

	String getCode();

	String getMessage();

	@Getter
	@RequiredArgsConstructor
	public static enum CommonErrorCode implements ErrorCodeEnum {

		INVALID_PARAMETER("COMMON-001", "파라미터가 유효하지 않습니다."), 
		UNCHECKED_ERROR("COMMON-999", "시스템에 오류가 발생했습니다.");

		private final String code;
		private final String message;
	}
	
	@Getter
	@RequiredArgsConstructor
	public static enum MemberErrorCode implements ErrorCodeEnum {

		NOT_FOUND_MEMBER("MEMBER-001", "존재하지 않는 회원입니다."),
		NOT_FOUND_MEMBER_USERNAME("MEMBER-002", "존재하지 않는 계정입니다."), 
		DUPLICATE_MEMBER_USERNAME("MEMBER-003", "동일한 계정의 회원이 존재합니다."), 
		INVALID_MEMBER_PASSWORD("MEMBER-004", "비밀번호가 일치하지 않습니다.");

		private final String code;
		private final String message;
	}

	@Getter
	@RequiredArgsConstructor
	public static enum PostErrorCode implements ErrorCodeEnum {

		NOT_FOUND_POST("POST-001", "존재하지 않는 게시글입니다."), 
		NOT_FOUND_POST_WRITER("POST-002", "존재하지 않는 작성자입니다.");

		private final String code;
		private final String message;
	}

}
