package com.jong.msa.board.core.web.validator;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Predicate;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@SuppressWarnings("unchecked")
public abstract class RequestValidator {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    protected void restControllerMethods() {}

	protected <T> Map.Entry<Predicate<T>, String> createValidEntry(Predicate<T> predicate, String message) {

		return new AbstractMap.SimpleEntry<>(predicate, message);
	}

	protected <T> ObjectError validate(T value, Map.Entry<Predicate<T>, String>... validEntries) {

		for (Map.Entry<Predicate<T>, String> validEntry : validEntries) {
			
			if (validEntry.getKey().test(value)) {
				
				return new ObjectError("request", validEntry.getValue());
			}
		}

		return null;
	}

	protected <T> FieldError validateField(String field, T value, Map.Entry<Predicate<T>, String>... validEntries) {

		for (Map.Entry<Predicate<T>, String> validEntry : validEntries) {
			
			if (validEntry.getKey().test(value)) {
				
				return new FieldError("request", field, validEntry.getValue());
			}
		}

		return null;
	}

}
