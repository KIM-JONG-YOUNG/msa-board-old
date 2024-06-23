package com.jong.msa.board.client.search.request;

import java.util.List;

import com.jong.msa.board.common.enums.Gender;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.common.enums.MemberSort;
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
public class MemberSearchRequest {
	
	@Builder.Default
	@Schema(description = "조회 시작 행", example = "1")
	private long offset = 1;
 
	@Builder.Default
	@Schema(description = "조회 행의 수", example = "10")
	private long limit = 10;

	@Schema(description = "정렬 조건 목록")
	private List<SortOrder<MemberSort>> sortOrderList;

	@Schema(description = "조회 조건", implementation = MemberSearchRequest.Condition.class)
	private Condition condition; 

	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Condition {

		@Schema(description = "계정", example = "username")
		private String username;
		
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

		@Schema(description = "생성 일자 검색")
		private DateRange createdDate;

		@Schema(description = "생성 일자 검색")
		private DateRange updatedDate;

	}

}
