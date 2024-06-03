package com.jong.msa.board.core.web.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.jong.msa.board.common.constants.Patterns;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class JavaTimeParamConverter<T> implements Converter<String, T> {

    protected final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Patterns.TIME_FORMAT);
    protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Patterns.DATE_FORMAT);
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Patterns.DATE_TIME_FORMAT);
    
	@Component
	public static class LocalTimeParamConverter extends JavaTimeParamConverter<LocalTime> {

		@Override
		public LocalTime convert(String source) {
			
			return LocalTime.parse(source, timeFormatter);
		}
	}

	@Component
	public static class LocalDateParamConverter extends JavaTimeParamConverter<LocalDate> {

		@Override
		public LocalDate convert(String source) {
			
			return LocalDate.parse(source, dateFormatter);
		}
	}

	@Component
	public static class LocalDateTimeParamConverter extends JavaTimeParamConverter<LocalDateTime> {

		@Override
		public LocalDateTime convert(String source) {
			
			return LocalDateTime.parse(source, dateTimeFormatter);
		}
	}

}
