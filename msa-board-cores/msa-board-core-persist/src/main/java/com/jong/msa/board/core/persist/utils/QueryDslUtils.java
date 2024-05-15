package com.jong.msa.board.core.persist.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;

public final class QueryDslUtils {

	public static <E extends Enum<E>> BooleanExpression equalsIfPresent(EnumPath<E> column, E value) {

		return (value != null) ? column.eq(value) : null;
	}

	public static BooleanExpression equalsIfPresent(StringPath column, String value) {

		return (StringUtils.hasText(value)) ? column.eq(value) : null;
	}

	public static BooleanExpression containIfPresent(StringPath column, String value) {

		return (StringUtils.hasText(value)) ? column.contains(value) : null;
	}

	public static BooleanExpression afterIfPresent(DateTimePath<LocalDateTime> column, LocalDate from) {
		
		return (from != null) ? column.after(LocalDateTime.of(from, LocalTime.of(0, 0, 0))) : null;
	}

	public static BooleanExpression beforeIfPresent(DateTimePath<LocalDateTime> column, LocalDate to) {
		
		return (to != null) ? column.after(LocalDateTime.of(to, LocalTime.of(23, 59, 59))) : null;
	}

}
