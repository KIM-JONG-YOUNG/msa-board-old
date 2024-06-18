package com.jong.msa.board.client.search.request;

import java.time.LocalDate;

import com.jong.msa.board.common.enums.Gender;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.common.enums.MemberSort;
import com.jong.msa.board.common.enums.State;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchMemberRequest extends PagingRequest<MemberSort> {
	
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

	@Schema(description = "생성 일자 검색 시작 일자")
	private LocalDate createdDateFrom;

	@Schema(description = "생성 일자 검색 종료 일자")
	private LocalDate createdDateTo;

	@Schema(description = "수정 일자 검색 시작 일자")
	private LocalDate updatedDateFrom;

	@Schema(description = "수정 일자 검색 종료 일자")
	private LocalDate updatedDateTo;

}
