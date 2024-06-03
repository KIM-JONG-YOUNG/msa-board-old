package com.jong.msa.board.common.constants;

/**
 * Primary Type 및 String 타입의 상수는 사용한 클래스를 컴파일하는 과정에서 
 * 값이 컴파일된 Class에 포함됨 
 */
public final class Patterns {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
	public static final String EMAIL_PATTERN = "^[0-9a-zA-Z-_.]+@[0-9a-zA-Z-_.]+.[a-zA-Z]{2,3}$";
	
}
