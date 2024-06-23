package com.jong.msa.board.client.member.request;

import com.jong.msa.board.common.enums.Gender;
import com.jong.msa.board.common.enums.Group;
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
public class MemberModifyRequest {

	@Schema(description = "이름", example = "name")
	private String name;
	
	@Schema(description = "성별", example = "MAIL")
	private Gender gender;
	
	@Schema(description = "이메일", example = "test@example.com")
	private String email;
	
	@Schema(description = "그룹", example = "ADMIN")
	private Group group;

	@Schema(description = "상태", example = "ACTIVE")
	private State state;

}
