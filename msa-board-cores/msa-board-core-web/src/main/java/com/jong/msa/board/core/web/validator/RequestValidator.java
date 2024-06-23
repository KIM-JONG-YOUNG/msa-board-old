package com.jong.msa.board.core.web.validator;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;

import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.dto.ErrorDetails;
import com.jong.msa.board.core.web.exception.RestServiceException;

public abstract class RequestValidator {

	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
	protected void restControllerMethods() {}

	protected <T> Map.Entry<Predicate<T>, String> validation(Predicate<T> predicate, String message) {
 
		return new AbstractMap.SimpleEntry<>(predicate, message);
	}

	@SuppressWarnings("unchecked")
	protected <T> ErrorDetails validateField(String field, T value, Map.Entry<Predicate<T>, String>... fieldValidations) {

		for (Map.Entry<Predicate<T>, String> fieldValidation : fieldValidations) {
			
			if (fieldValidation.getKey().test(value)) {
				
				return ErrorDetails.builder()
						.field(field)
						.message(fieldValidation.getValue())
						.build();
			}
		}
		
		return null;
	}
	
	protected void validateRequest(ErrorDetails... errorDetailsArr) {

		List<ErrorDetails> errorDetailsList = Arrays.stream(errorDetailsArr)
				.filter(x -> x != null).collect(Collectors.toList());
		
		if (errorDetailsList.size() > 0) {
			
			throw new RestServiceException(
					HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PARAMETER, errorDetailsList);
		} 
	}

}
