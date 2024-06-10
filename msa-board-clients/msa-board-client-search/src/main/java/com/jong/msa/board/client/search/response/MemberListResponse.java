package com.jong.msa.board.client.search.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jong.msa.board.common.enums.CodeEnum.Gender;
import com.jong.msa.board.common.enums.CodeEnum.Group;
import com.jong.msa.board.common.enums.CodeEnum.State;

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
public class MemberListResponse extends SearchListResponse<MemberListResponse.Item> {

	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(name = "MemberListInfoResponse.Item")
	public static class Item {

		@Schema(description = "ID")
		private UUID id;
		
		@Schema(description = "계정")
		private String username;
		
		@Schema(description = "이름")
		private String name;
		
		@Schema(description = "성별")
		private Gender gender;
		
		@Schema(description = "이메일")
		private String email;
		
		@Schema(description = "그룹")
		private Group group;
		
		@Schema(description = "생성일시")
		private LocalDateTime createdDateTime;
		
		@Schema(description = "수정일시")
		private LocalDateTime updatedDateTime;
		
		@Schema(description = "상태")
		private State state;
	
	}
	
}
