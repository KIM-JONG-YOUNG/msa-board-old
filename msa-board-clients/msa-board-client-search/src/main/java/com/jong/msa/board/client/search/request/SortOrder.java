package com.jong.msa.board.client.search.request;

import com.jong.msa.board.common.enums.Order;
import com.jong.msa.board.common.enums.SortEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SortOrder<E extends SortEnum> {

	private E sort;
	
	private Order order;
	
}
