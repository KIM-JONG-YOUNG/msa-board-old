package com.jong.msa.board.endpoint.admin.request;

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
public class AdminLoginRequest {

	@Schema(description = "계정", example = "username")
	private String username;
	
	@Schema(description = "비밀번호" , example = "password")
	private String password;
	
}
