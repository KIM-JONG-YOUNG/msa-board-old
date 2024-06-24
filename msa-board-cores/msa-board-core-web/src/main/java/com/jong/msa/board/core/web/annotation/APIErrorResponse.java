package com.jong.msa.board.core.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCode;

@Inherited
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface APIErrorResponse {

	HttpStatus status();

	ErrorCode errorCode();

	boolean useErrorDetailsList() default false;

}
