package com.jong.msa.board.client.member.enums;

import com.jong.msa.board.core.web.enums.ErrorCodeEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCodeEnum {

	DUPLICATE_MEMBER_USERNAME("MEMBER-001", "동일한 회원 계정이 존재합니다."),
	NOT_FOUND_MEMBER("MEMBER-002", "존재하지 않는 회원입니다."),
	NOT_FOUND_MEMBER_USERNAME("MEMBER-003", "존재하지 않는 회원 계정입니다."),
	NOT_MATCHED_MEMBER_PASSWORD("MEMBER-004", "비밀번호가 일치하지 않습니다.");

	private final String code;
	
	private final String message;
	
}
