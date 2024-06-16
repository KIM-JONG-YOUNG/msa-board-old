package com.jong.msa.board.client.search.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.common.enums.State;

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
public class PostListResponse extends SearchListResponse<PostListResponse.Item> {
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(name = "PostListResponse.Item")
	public static class Item {

		@Schema(description = "ID")
		private UUID id;

		@Schema(description = "제목")
		private String title;

		@Schema(description = "내용")
		private String content;

		@Schema(description = "작성자")
		private Writer writer;

		@Schema(description = "조회수")
		private int views;

		@Schema(description = "생성일시")
		private LocalDateTime createdDateTime;

		@Schema(description = "수정일시")
		private LocalDateTime updatedDateTime;

		@Schema(description = "상태")
		private State state;

	}
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(name = "PostListResponse.Writer")
	public static class Writer {

		@Schema(description = "ID")
		private UUID id;

		@Schema(description = "계정")
		private String username;

		@Schema(description = "이름")
		private String name;

		@Schema(description = "그룹")
		private Group group;

	}

}
