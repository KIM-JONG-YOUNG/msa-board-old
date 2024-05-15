package com.jong.msa.board.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.util.StringUtils;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Constraint(validatedBy = StringValidate.Validator.class)
public @interface StringValidate {

	String message() default "";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	BlankCheck blankCheck() default @BlankCheck(used = false, message = "");
	
	SizeCheck sizeCheck() default @SizeCheck(used = false, message = ""); 

	PatternCheck patternCheck() default @PatternCheck(used = false, regexp = "", message = ""); 

	public static class Validator implements ConstraintValidator<StringValidate, String> {

		private StringValidate annotation;
		
		@Override
		public void initialize(StringValidate constraintAnnotation) {
			
			this.annotation = constraintAnnotation;
		}
		
		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {

			BlankCheck blankCheck = annotation.blankCheck();
			SizeCheck sizeCheck = annotation.sizeCheck();
			PatternCheck patternCheck = annotation.patternCheck();
			
			if (blankCheck.used() && !validate(value, blankCheck, context)) {
				return false;
			} else if (sizeCheck.used() && !validate(value, sizeCheck, context)) {
				return false;
			} else if (patternCheck.used() && !validate(value, patternCheck, context)) {
				return false;
			} else {
				return true;
			}
		}

		private void setMessage(ConstraintValidatorContext context, String message) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		}
		
		private boolean validate(String value, BlankCheck blankCheck, ConstraintValidatorContext context) {

			if (value == null && blankCheck.nullable()) {
				return true;
			} else if (StringUtils.hasText(value)) {
				return true;
			} else {
				setMessage(context, blankCheck.message());
				return false;
			}
		}

		private boolean validate(String value, SizeCheck sizeCheck, ConstraintValidatorContext context) {

			int min = sizeCheck.min();
			int max = sizeCheck.max();
			
			if (value == null) {
				return true;
			} else if (min <= value.length() && value.length() <= max) {
				return true;
			} else {
				setMessage(context, sizeCheck.message());
				return false;
			}
		}

		private boolean validate(String value, PatternCheck patternCheck, ConstraintValidatorContext context) {

			String regexp = patternCheck.regexp();

			if (value == null) {
				return true;
			} else if (Pattern.matches(regexp, value)) {
				return true;
			} else {
				setMessage(context, patternCheck.message());
				return false;
			}
		}

	}

	public interface CheckAnnotation {}
	
	@Documented
	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface BlankCheck {

		boolean used() default true;	

		boolean nullable() default false;
		
		String message();
		
	}

	@Documented
	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SizeCheck {

		boolean used() default true;
		
		int min() default 0;

		int max() default 10;

		String message();
		
	}

	@Documented
	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface PatternCheck {

		boolean used() default true;

		String regexp();

		String message();
		
	}
	
}
