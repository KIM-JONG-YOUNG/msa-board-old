package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State implements CodeEnum<Integer> {

	ACTIVE(1), DEACTIVE(0);

	private final Integer code;
	
}