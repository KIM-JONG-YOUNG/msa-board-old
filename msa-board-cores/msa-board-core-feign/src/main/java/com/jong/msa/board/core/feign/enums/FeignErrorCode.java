package com.jong.msa.board.core.feign.enums;

import com.jong.msa.board.core.web.enums.ErrorCodeEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeignErrorCode implements ErrorCodeEnum {

	UNCHECKED_GATEWAY_ERROR("GATEWAY-001", "내부 시스템을 호출하는데 오류가 발생했습니다.");
	
	private final String code;
	
	private final String message;
	
}
