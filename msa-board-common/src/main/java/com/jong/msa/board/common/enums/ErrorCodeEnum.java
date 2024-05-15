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

}
