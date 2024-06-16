package com.jong.msa.board.core.web.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCodeEnum {

	INVALID_PARAMETER("COMMON-001", "파라미터가 유효하지 않습니다."),
	UNCHECKED_SYSTEM_ERROR("COMMON-999", "시스템에 오류가 발생했습니다.");
	
	private final String code;
	
	private final String message;
	
}
