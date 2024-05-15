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

	@Getter
	@RequiredArgsConstructor
	public static enum TokenErrorCode implements ErrorCodeEnum {
		
		EXPIRED_ACCESS_TOKEN("TOKEN-001", "만료된 Access Token 입니다."),
		REVOKED_ACCESS_TOKEN("TOKEN-002", "사용할 수 없는 Access Token 입니다."),
		INVALID_ACCESS_TOKEN("TOKEN-003", "유효하지 않은 Access Token 입니다."),

		EXPIRED_REFRESH_TOKEN("TOKEN-011", "만료된 Refresh Token 입니다."),
		REVOKED_REFRESH_TOKEN("TOKEN-012", "사용할 수 없는 Refresh Token 입니다."),
		INVALID_REFRESH_TOKEN("TOKEN-013", "유효하지 않은 Refresh Token 입니다.");

		private final String code;
		private final String message;
	}

	@Getter
	@RequiredArgsConstructor
	public static enum SecurityErrorCode implements ErrorCodeEnum {
		
		UNAUTHORIZED_MEMBER("SECURITY-001", "인증되지 않은 회원입니다."), 
		NOT_ACCESSIBLE_MEMBER("SECURITY-002", "접근할 수 없는 회원입니다.");

		private final String code;
		private final String message; 
	}

	@Getter
	@RequiredArgsConstructor
	public static enum AdminErrorCode implements ErrorCodeEnum {
		
		NOT_ADMIN_GROUP_USERNAME("ADMIN-001", "관리자 회원의 계정이 아닙니다."),
		NOT_ADMIN_GROUP_POST("ADMIN-002", "관리자 게시글이 아닙니다."),
		NOT_USER_GROUP_MEMBER("ADMIN-003", "일반 그룹의 회원이 아닙니다.");
		
		private final String code;
		private final String message;
	}
	
	@Getter
	@RequiredArgsConstructor
	public static enum UserErrorCode implements ErrorCodeEnum {
		
		NOT_USER_GROUP_USERNAME("USER-001", "일반 회원의 계정이 아닙니다."),
		NOT_POST_WRITER("USER-002", "게시글 작성자가 아닙니다.");
		
		private final String code;
		private final String message;
	}
	
}
