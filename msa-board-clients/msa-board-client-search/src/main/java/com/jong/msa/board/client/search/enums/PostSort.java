package com.jong.msa.board.client.search.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostSort implements SortEnum {

	TITLE(Order.ASC), 
	CONTENT(Order.ASC), 
	VIEWS(Order.DESC), 
	WRITER(Order.ASC), 
	CREATED_DATE_TIME(Order.DESC), 
	UPDATED_DATE_TIME(Order.DESC);
	
	private final Order defaultOrder;
	
}
