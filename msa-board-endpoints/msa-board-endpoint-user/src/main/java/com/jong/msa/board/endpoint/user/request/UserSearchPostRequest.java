package com.jong.msa.board.endpoint.user.request;

import javax.validation.Valid;

import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.request.SearchRequest;
import com.jong.msa.board.client.search.request.param.DateRange;

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
public class UserSearchPostRequest extends SearchRequest<UserSearchPostRequest.Condition, SearchPostRequest.Sort> {
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(name = "UserSearchPostRequest.Condtion")
	public static class Condition {

		@Schema(description = "제목", example = "title")
		private String title;

		@Schema(description = "내용", example = "content")
		private String content;

		@Schema(description = "작성자 계정", example = "username")
		private String writerUsername;

		@Valid
		@Schema(description = "생성일자 검색기간")
		private DateRange createdDate;

		@Valid
		@Schema(description = "수정일자 검색기간")
		private DateRange updatedDate;

	}

}
