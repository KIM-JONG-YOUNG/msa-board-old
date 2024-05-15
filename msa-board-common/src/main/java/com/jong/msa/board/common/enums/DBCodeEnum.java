package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * API 파라미터 및 DB에 저장하기 위한 Enum 인터페이스  
 * 
 * @param <V> DB에 저장될 코드 값의 타입 
 */
public interface DBCodeEnum<V> {

	V getCode();

	@Getter
	@RequiredArgsConstructor
	public static enum Gender implements DBCodeEnum<Character> {

		MAIL('M'), FEMAIL('F');
	 
		private final Character code;
	}
	
	@Getter
	@RequiredArgsConstructor
	public static enum Group implements DBCodeEnum<Integer> {

		ADMIN(1), USER(2);

		private final Integer code;
	}

	@Getter
	@RequiredArgsConstructor
	public static enum State implements DBCodeEnum<Integer> {

		ACTIVE(1), DEACTIVE(0);

		private final Integer code;
	}

}