package com.jong.msa.board.core.web.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

import com.jong.msa.board.common.constants.Patterns;

public class LocalDateParamConverter implements Converter<String, LocalDate> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Patterns.DATE_FORMAT);

	@Override
	public LocalDate convert(String source) {
		
		return LocalDate.parse(source, FORMATTER);
	}

}
