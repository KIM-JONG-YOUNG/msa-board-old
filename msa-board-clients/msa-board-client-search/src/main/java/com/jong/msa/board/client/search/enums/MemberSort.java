package com.jong.msa.board.client.search.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberSort implements SortEnum {

	USERNAME(Order.ASC), 
	NAME(Order.ASC), 
	EMAIL(Order.ASC), 
	CREATED_DATE_TIME(Order.DESC), 
	UPDATED_DATE_TIME(Order.DESC);
	
	private final Order defaultOrder;
	
}
