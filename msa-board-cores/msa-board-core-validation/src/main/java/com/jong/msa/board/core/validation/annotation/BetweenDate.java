package com.jong.msa.board.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Constraint(validatedBy = BetweenDate.Validator.class)
public @interface BetweenDate {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	DateFields[] checkFields();

	public static class Validator implements ConstraintValidator<BetweenDate, Object> {

		private BetweenDate annotation;
 
		@Override
		public void initialize(BetweenDate constraintAnnotation) {

			this.annotation = constraintAnnotation;
		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {

			return Stream.of(annotation.checkFields())
					.map(checkFields -> validate(value, checkFields, context))
					.filter(isValid -> isValid)
					.count() == annotation.checkFields().length;
		}

		private void setMessage(ConstraintValidatorContext context, String field, String message) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addPropertyNode(field).addConstraintViolation();
		}

		private <T> T getFieldValue(Object value, String fieldName, Class<T> fieldType) {

			try {

				Field field = value.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);

				return fieldType.cast(field.get(value));

			} catch (Exception e) {

				throw new RuntimeException("객체에서 필드를 조회하는데 오류가 발생했습니다..", e);
			}
		}
		
		private boolean validate(Object value, DateFields dateFields, ConstraintValidatorContext context) {

			DateType dateType = dateFields.dateType();
			String fromFieldName = dateFields.fromField();
			String toFieldName = dateFields.toField();
			String message = dateFields.message();

			switch (dateType) {
			case TIME:

				LocalTime fromTime = getFieldValue(value, fromFieldName, LocalTime.class);
				LocalTime toTime = getFieldValue(value, toFieldName, LocalTime.class);

				if (fromTime != null && toTime != null && fromTime.isAfter(toTime)) {
					setMessage(context, fromFieldName, message);
					return false;
				} else {
					return true;
				}
				
			case DATE:

				LocalDate fromDate = getFieldValue(value, fromFieldName, LocalDate.class);
				LocalDate toDate = getFieldValue(value, toFieldName, LocalDate.class);

				if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
					setMessage(context, fromFieldName, message);
					return false;
				} else {
					return true;
				}
				
			case DATE_TIME:
				
				LocalDateTime fromDateTime = getFieldValue(value, fromFieldName, LocalDateTime.class);
				LocalDateTime toDateTime = getFieldValue(value, toFieldName, LocalDateTime.class);

				if (fromDateTime != null && toDateTime != null && fromDateTime.isAfter(toDateTime)) {
					setMessage(context, fromFieldName, message);
					return false;
				} else {
					return true;
				}
				
			default: 
				
				throw new RuntimeException("적절하지 않은 DateType 입니다.");
			}
		}

	}

	@Documented
	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DateFields {

		DateType dateType() default DateType.DATE;
		
		String fromField();

		String toField();

		String message();

	}

	public static enum DateType {
		
		TIME, DATE, DATE_TIME;
	}
	
}
