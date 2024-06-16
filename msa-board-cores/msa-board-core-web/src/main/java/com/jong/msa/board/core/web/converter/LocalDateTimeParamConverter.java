package com.jong.msa.board.core.web.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

import com.jong.msa.board.common.constants.Patterns;

public class LocalDateTimeParamConverter implements Converter<String, LocalDateTime> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Patterns.DATE_TIME_FORMAT);

	@Override
	public LocalDateTime convert(String source) {

		return LocalDateTime.parse(source, FORMATTER);
	}

}
