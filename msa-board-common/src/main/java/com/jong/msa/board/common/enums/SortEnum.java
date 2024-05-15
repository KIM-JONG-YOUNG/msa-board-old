package com.jong.msa.board.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 검색 시 정렬을 위한 Enum의 인터페이스 
 */
public interface SortEnum {
	
	Order getDefaultOrder();

	public static enum Order {
		
		ASC, DESC;
	}

	@Getter
	@RequiredArgsConstructor
	public static enum MemberSort implements SortEnum {

		USERNAME(Order.ASC),
		NAME(Order.ASC),
		EMAIL(Order.ASC),
		CREATED_DATE_TIME(Order.DESC),
		UPDATED_DATE_TIME(Order.DESC);
		
		private final Order defaultOrder;
	}
	
	@Getter
	@RequiredArgsConstructor
	public static enum PostSort implements SortEnum {
		
		TITLE(Order.ASC),
		CONTENT(Order.ASC),
		WRITER_USERNAME(Order.ASC),
		VIEWS(Order.DESC),
		CREATED_DATE_TIME(Order.DESC),
		UPDATED_DATE_TIME(Order.DESC);
		
		private final Order defaultOrder;
		
	}

}
