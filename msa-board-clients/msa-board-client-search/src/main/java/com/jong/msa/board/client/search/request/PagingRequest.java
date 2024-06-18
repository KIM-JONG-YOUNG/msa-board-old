package com.jong.msa.board.client.search.request;

import com.jong.msa.board.common.enums.Order;
import com.jong.msa.board.common.enums.SortEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PagingRequest<E extends SortEnum> {

	@Builder.Default
	@Schema(description = "조회 시작 행 번호", example = "0")
	private long offset = 0;
	
	@Builder.Default
	@Schema(description = "조회 행의 수", example = "10")
	private int limit = 10;

	@Schema(description = "정렬 필드")
	private E sort;
	
	@Schema(description = "정렬 방식")
	private Order order;

}
