package com.jong.msa.board.core.security.enums;

import com.jong.msa.board.core.web.enums.ErrorCodeEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCodeEnum {

	EXPIRED_ACCESS_TOKEN("SECURITY-001", "만료된 Access Token 입니다."), 
	REVOKE_ACCESS_TOKEN("SECURITY-002", "사용할 수 없는 Access Token 입니다."), 
	INVALID_ACCESS_TOKEN("SECURITY-003", "유효하지 않은 Access Token 입니다."), 
	EXPIRED_REFRESH_TOKEN("SECURITY-004", "만료된 Refresh Token 입니다."), 
	REVOKE_REFRESH_TOKEN("SECURITY-005", "사용할 수 없는 Refresh Token 입니다."), 
	INVALID_REFRESH_TOKEN("SECURITY-006", "유효하지 않은 Refresh Token 입니다."), 
	NOT_ACCESSIBLE_URL("SECURITY-007", "접근할 수 없는 URL 입니다.");
	
	private final String code;
	
	private final String message;
	
}
