package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Group implements CodeEnum<Integer> {

	ADMIN(1), USER(2);

	private final Integer code;
	
}
