package com.jong.msa.board.core.validation.utils;

import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

public final class BindingResultUtils {

	public static BindingResult createBindingResult(Object object) {

		return new BeanPropertyBindingResult(object, StringUtils.uncapitalize(object.getClass().getSimpleName()));
	}

	public static BindingResult validate(Object object, Validator validator) {

		BindingResult bindingResult = createBindingResult(object);
		
		validator.validate(object, bindingResult);
		
		return bindingResult;
	}

}
