package com.jong.msa.board.client.member.request;

import javax.validation.constraints.NotNull;

import com.jong.msa.board.common.enums.Gender;
import com.jong.msa.board.common.enums.Group;

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
public class MemberCreateRequest {

	@Schema(description = "계정", example = "username")
	private String username;
	
	@Schema(description = "비밀번호" , example = "password")
	private String password;
	
	@Schema(description = "이름", example = "name")
	private String name;
	
	@Schema(description = "성별", example = "MAIL")
	@NotNull(message = "성별은 비어있을 수 없습니다.")
	private Gender gender;
	
	@Schema(description = "이메일", example = "test@example.com")
	private String email;
	
	@Schema(description = "그룹", example = "ADMIN")
	private Group group;
	
}
