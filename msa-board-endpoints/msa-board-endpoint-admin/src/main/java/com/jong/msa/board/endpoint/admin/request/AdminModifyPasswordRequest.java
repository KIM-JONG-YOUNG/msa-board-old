package com.jong.msa.board.endpoint.admin.request;

import com.jong.msa.board.core.validation.annotation.StringValidate;
import com.jong.msa.board.core.validation.annotation.StringValidate.BlankCheck;

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
public class AdminModifyPasswordRequest {

	@Schema(description = "현재 비밀번호" , example = "currentPassword")
	@StringValidate(
			blankCheck = @BlankCheck(message = "현재 비밀번호는 비어있을 수 없습니다."))
	private String currentPassword;
	
	@Schema(description = "새로운 비밀번호" , example = "newPassword")
	@StringValidate(
			blankCheck = @BlankCheck(message = "새로운 비밀번호는 비어있을 수 없습니다."))
	private String newPassword;

}
