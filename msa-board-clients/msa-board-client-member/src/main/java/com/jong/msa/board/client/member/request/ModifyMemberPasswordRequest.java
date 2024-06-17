package com.jong.msa.board.client.member.request;

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
public class ModifyMemberPasswordRequest {

	@Schema(description = "현재 비밀번호" , example = "currentPassword")
	private String currentPassword;
	
	@Schema(description = "새로운 비밀번호" , example = "newPassword")
	private String newPassword;

}
