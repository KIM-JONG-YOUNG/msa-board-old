package com.jong.msa.board.client.post.request;

import java.util.UUID;

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
public class CreatePostRequest {

	@Schema(description = "제목", example = "title")
	private String title;

	@Schema(description = "내용", example = "content")
	private String content;

	@Schema(description = "작성자 ID")
	private UUID writerId;

}
