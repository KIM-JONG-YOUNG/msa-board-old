package com.jong.msa.board.client.search.request;

import javax.validation.Valid;

import com.jong.msa.board.client.search.enums.MemberSort;
import com.jong.msa.board.client.search.request.param.DateRange;
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
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchMemberRequest extends SearchRequest<SearchMemberRequest.Condition, MemberSort> {
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(name = "MemberSearchRequest.Condtion")
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

		@Valid
		@Schema(description = "생성일자 검색기간")
		private DateRange createdDate;

		@Valid
		@Schema(description = "수정일자 검색기간")
		private DateRange updatedDate;

	}

}
