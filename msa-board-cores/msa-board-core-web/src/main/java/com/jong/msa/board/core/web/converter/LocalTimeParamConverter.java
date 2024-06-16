package com.jong.msa.board.core.web.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

import com.jong.msa.board.common.constants.Patterns;

public class LocalTimeParamConverter implements Converter<String, LocalTime> {

	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Patterns.TIME_FORMAT);

	@Override
	public LocalTime convert(String source) {

		return LocalTime.parse(source, FORMATTER);
	}

}
