package com.jong.msa.board.endpoint.request;

import com.jong.msa.board.core.validation.annotation.StringValidate;
import com.jong.msa.board.core.validation.annotation.StringValidate.BlankCheck;
import com.jong.msa.board.core.validation.annotation.StringValidate.SizeCheck;

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
public class UserWritePostRequest {

	@Schema(description = "제목", example = "title")
	@StringValidate(
			blankCheck = @BlankCheck(message = "제목은 비어있을 수 없습니다."),
			sizeCheck = @SizeCheck(max = 300, message = "제목은 300자를 초과할 수 없습니다."))
	private String title;

	@Schema(description = "내용", example = "content")
	@StringValidate(
			blankCheck = @BlankCheck(message = "내용은 비어있을 수 없습니다."))
	private String content;

}
