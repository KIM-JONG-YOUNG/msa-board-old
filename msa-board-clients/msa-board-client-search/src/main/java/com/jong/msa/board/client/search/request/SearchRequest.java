package com.jong.msa.board.client.search.request;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.jong.msa.board.client.search.enums.SortEnum;
import com.jong.msa.board.client.search.request.param.SortOrder;

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
public abstract class SearchRequest<T, E extends SortEnum> {

	@Valid
	@Schema(description = "검색 조건")
	private T condition;

	@Valid
	@Schema(description = "정렬 조건")
	private SortOrder<E> sortOrder;

	@Builder.Default
	@Schema(description = "조회 시작 행 번호", example = "0")
	@PositiveOrZero(message = "조회 시작 행 번호는 O 보다 작을 수 없습니다.")
	private long offset = 0;
	
	@Builder.Default
	@Schema(description = "조회 행의 수", example = "10")
	@Positive(message = "조회 행의 수는 1 보다 작을 수 없습니다.")
	private int limit = 10;
	
}
