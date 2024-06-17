package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum ErrorCode {

	INVALID_PARAMETER(400, "파라미터가 유효하지 않습니다."),
	UNCHECKED_INTERNAL_ERROR(500, "내부 시스템에 오류가 발생했습니다."),
	UNCHECKED_EXTERNAL_ERROR(503, "외부 시스템에 오류가 발생했습니다."),
	
	DUPLICATE_USERNAME(409, "중복된 계정이 존재합니다."),
	NOT_FOUND_MEMBER(410, "존재하지 않는 회원입니다."),
	NOT_MATCHED_PASSWORD(400, "비밀번호가 일치하지 않습니다."),

	NOT_FOUND_POST(410, "존재하지 않는 게시글입니다."),
	NOT_FOUND_POST_WRITER(410, "존재하지 않는 작성자입니다."),

	EXPIRED_ACCESS_TOKEN(401, "만료된 Access Token입니다."),
	REVOKED_ACCESS_TOKEN(401, "사용할 수 없는 Access Token입니다."),
	INVALID_ACCESS_TOKEN(401, "유효하지 않은 Access Token입니다."),
	EXPIRED_REFRESH_TOKEN(401, "만료된 Refresh Token입니다."),
	REVOKED_REFRESH_TOKEN(401, "사용할 수 없는 Refresh Token입니다."),
	INVALID_REFRESH_TOKEN(401, "유효하지 않은 Refresh Token입니다."),

	NOT_ACCESSIBLE_URL(403, "접근할 수 없는 URL 입니다."),

	;
	
	private final int statusCode;
	
	private final String message;
	
}
