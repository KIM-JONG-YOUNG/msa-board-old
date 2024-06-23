package com.jong.msa.board.client.search.request;

import java.util.List;

import com.jong.msa.board.common.enums.PostSort;
import com.jong.msa.board.common.enums.State;

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
public class PostSearchRequest {
	
	@Builder.Default
	@Schema(description = "조회 시작 행", example = "1")
	private long offset = 1;
 
	@Builder.Default
	@Schema(description = "조회 행의 수", example = "10")
	private long limit = 10;

	@Schema(description = "정렬 조건 목록")
	private List<SortOrder<PostSort>> sortOrderList;

	@Schema(description = "조회 조건", implementation = PostSearchRequest.Condition.class)
	private Condition condition;

	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Condition {

		@Schema(description = "제목", example = "title")
		private String title;

		@Schema(description = "내용", example = "content")
		private String content;

		@Schema(description = "작성자 계정", example = "username")
		private String writerUsername;

		@Schema(description = "상태", example = "ACTIVE")
		private State state;
		
		@Schema(description = "생성 일자 검색")
		private DateRange createdDate;

		@Schema(description = "생성 일자 검색")
		private DateRange updatedDate;
		
	}

}
