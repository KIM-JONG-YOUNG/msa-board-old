package com.jong.msa.board.client.search.request;

import java.time.LocalDate;

import com.jong.msa.board.common.enums.PostSort;
import com.jong.msa.board.common.enums.State;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchPostRequest extends SearchRequest<PostSort> {
	
	@Schema(description = "제목", example = "title")
	private String title;

	@Schema(description = "내용", example = "content")
	private String content;

	@Schema(description = "작성자 계정", example = "username")
	private String writerUsername;

	@Schema(description = "상태", example = "ACTIVE")
	private State state;
	
	@Schema(description = "생성일자 검색 시작 일자")
	private LocalDate createdDateFrom;

	@Schema(description = "생성일자 검색 종료 일자")
	private LocalDate createdDateTo;

	@Schema(description = "수정일자 검색 시작 일자")
	private LocalDate updatedDateFrom;

	@Schema(description = "수정일자 검색 종료 일자")
	private LocalDate updatedDateTo;

}
