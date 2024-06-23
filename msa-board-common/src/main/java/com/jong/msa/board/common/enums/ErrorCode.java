package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum ErrorCode implements CodeEnum<String> {

	INVALID_PARAMETER("PARAM-001", "파라미터가 유효하지 않습니다."),

	NOT_ACCESSIBLE_URL("ACCESS-001", "접근할 수 없는 URL 입니다."),

	DUPLICATE_USERNAME("MEMBER-001", "중복된 계정이 존재합니다."),
	NOT_FOUND_MEMBER("MEMBER-002", "존재하지 않는 회원입니다."),
	NOT_MATCHED_PASSWORD("MEMBER-003", "비밀번호가 일치하지 않습니다."),
	NOT_ADMIN_USERNAME("MEMBER-004", "관리자 회원의 계정이 아닙니다."),
	NOT_USER_USERNAME("MEMBER-005", "일반 회원의 계정이 아닙니다."),
	NOT_USER_GROUP_MEMBER("MEMBER-006", "일반 회원이 아닙니다."),

	NOT_FOUND_POST("POST-001", "존재하지 않는 게시글입니다."),
	NOT_FOUND_POST_WRITER("POST-002", "존재하지 않는 작성자입니다."),
	NOT_POST_WRITER("POST-003", "게시글 작성자가 아닙니다."),
	NOT_ADMIN_POST("POST-004", "관리자 게시글이 아닙니다."),
	DEACTIVE_POST("POST-005", "삭제된 게시글이 입니다."),
	
	EXPIRED_ACCESS_TOKEN("TOKEN-001", "만료된 Access Token입니다."),
	REVOKED_ACCESS_TOKEN("TOKEN-002", "사용할 수 없는 Access Token입니다."),
	INVALID_ACCESS_TOKEN("TOKEN-003", "유효하지 않은 Access Token입니다."),
	EXPIRED_REFRESH_TOKEN("TOKEN-004", "만료된 Refresh Token입니다."),
	REVOKED_REFRESH_TOKEN("TOKEN-005", "사용할 수 없는 Refresh Token입니다."),
	INVALID_REFRESH_TOKEN("TOKEN-006", "유효하지 않은 Refresh Token입니다."),
	
	UNCHECKED_INTERNAL_ERROR("ERROR-003", "내부 시스템에 오류가 발생했습니다."),
	UNCHECKED_EXTERNAL_ERROR("ERROR-004", "외부 시스템에 오류가 발생했습니다.");
	
	private final String code;
	private final String message;

}
