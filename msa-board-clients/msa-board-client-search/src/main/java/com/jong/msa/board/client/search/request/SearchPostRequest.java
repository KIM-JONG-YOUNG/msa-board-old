package com.jong.msa.board.client.search.request;

import javax.validation.Valid;

import com.jong.msa.board.client.search.request.param.DateRange;
import com.jong.msa.board.client.search.request.param.SortOrder;
import com.jong.msa.board.client.search.request.param.SortOrder.Order;
import com.jong.msa.board.common.enums.CodeEnum.State;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchPostRequest extends SearchRequest<SearchPostRequest.Condition, SearchPostRequest.Sort> {
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(name = "PostSearchRequest.Condtion")
	public static class Condition {

		@Schema(description = "제목", example = "title")
		private String title;

		@Schema(description = "내용", example = "content")
		private String content;

		@Schema(description = "작성자 계정", example = "username")
		private String writerUsername;

		@Schema(description = "상태", example = "ACTIVE")
		private State state;

		@Valid
		@Schema(description = "생성일자 검색기간")
		private DateRange createdDate;

		@Valid
		@Schema(description = "수정일자 검색기간")
		private DateRange updatedDate;

	}

	@Getter
	@RequiredArgsConstructor
	public static enum Sort implements SortOrder.SortEnum {
		
		TITLE(Order.ASC),
		CONTENT(Order.ASC),
		WRITER_USERNAME(Order.ASC),
		VIEWS(Order.DESC),
		CREATED_DATE_TIME(Order.DESC),
		UPDATED_DATE_TIME(Order.DESC);
		
		private final Order defaultOrder;
	}
	
}
