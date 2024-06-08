package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CodeEnum<V> {

	V getCode();

	@Getter
	@RequiredArgsConstructor
	public static enum Gender implements CodeEnum<Character> {

		MAIL('M'), FEMAIL('F');

		private final Character code;
	}

	@Getter
	@RequiredArgsConstructor
	public static enum Group implements CodeEnum<Integer> {

		ADMIN(1), USER(2);

		private final Integer code;
	}

	@Getter
	@RequiredArgsConstructor
	public static enum State implements CodeEnum<Integer> {

		ACTIVE(1), DEACTIVE(0);

		private final Integer code;
	}

	@Getter
	@RequiredArgsConstructor
	public static enum ErrorCode implements CodeEnum<String> {

		INVALID_PARAMETER("COMMON-001", "파라미터가 유효하지 않습니다."), 
		INACCESSIBLE_URL("COMMON-002", "접근할 수 없는 URL입니다."),
		UNCHECKED_ERROR("COMMON-003", "시스템에 오류가 발생했습니다."),

		NOT_FOUND_MEMBER("MEMBER-001", "존재하지 않는 회원입니다."), 
		NOT_FOUND_MEMBER_USERNAME("MEMBER-002", "존재하지 않는 계정입니다."),
		DUPLICATED_MEMBER_USERNAME("MEMBER-003", "동일한 계정의 회원이 존재합니다."),
		NOT_MATCHED_MEMBER_PASSWORD("MEMBER-004", "비밀번호가 일치하지 않습니다."),
		NOT_ADMIN_GROUP_MEMBER_USERNAME("MEMBER-005", "관리자 회원의 계정이 아닙니다."),
		NOT_USER_GROUP_MEMBER_USERNAME("MEMBER-006", "일반 회원의 계정이 아닙니다."),
		NOT_USER_GROUP_MEMBER("MEMBER-007", "일반 회원이 아닙니다."), 

		NOT_FOUND_POST("POST-001", "존재하지 않는 게시글입니다."), 
		NOT_FOUND_POST_WRITER("POST-002", "존재하지 않는 작성자입니다."),
		NOT_POST_WRITER("POST-003", "게시글 작성자가 아닙니다."), 
		NOT_ADMIN_GROUP_POST("POST-004", "관리자 회원 게시글이 아닙니다."),
		DEACTIVE_POST("POST-005", "삭제된 게시글입니다."), 

		EXPIRED_ACCESS_TOKEN("TOKEN-001", "만료된 Access Token 입니다."),
		REVOKED_ACCESS_TOKEN("TOKEN-002", "사용할 수 없는 Access Token 입니다."),
		INVALID_ACCESS_TOKEN("TOKEN-003", "유효하지 않은 Access Token 입니다."),

		EXPIRED_REFRESH_TOKEN("TOKEN-004", "만료된 Refresh Token 입니다."),
		REVOKED_REFRESH_TOKEN("TOKEN-005", "사용할 수 없는 Refresh Token 입니다."),
		INVALID_REFRESH_TOKEN("TOKEN-006", "유효하지 않은 Refresh Token 입니다."),

		NOT_ADMIN_GROUP_REFRESH_TOKEN("TOKEN-007", "관리자 회원의 Refresh Token 입니다."),
		NOT_USER_GROUP_REFRESH_TOKEN("TOKEN-008", "일반 회원의 Refresh Token 입니다."),
;
		private final String code;
		private final String message;
	}

}