package com.jong.msa.board.core.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCode;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(APIErrorResponse.List.class)
public @interface APIErrorResponse {

	HttpStatus status();
	
	ErrorCode errorCode();

	boolean useErrorDetailsList() default false; 
	
	@Inherited
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public static @interface List {

		APIErrorResponse[] value();
		
	}

}
