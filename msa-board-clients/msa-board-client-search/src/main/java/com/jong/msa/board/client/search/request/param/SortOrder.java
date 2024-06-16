package com.jong.msa.board.client.search.request.param;

import javax.validation.constraints.NotNull;

import com.jong.msa.board.client.search.enums.Order;
import com.jong.msa.board.client.search.enums.SortEnum;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "정렬필드")
	@NotNull(message = "정렬필드는 비어있을 수 없습니다.")
	private E sort;
	
	@Schema(description = "정렬방식")
	private Order order;
	
}
