package com.jong.msa.board.client.search.request;

import java.time.LocalDate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.jong.msa.board.common.enums.DBCodeEnum.Gender;
import com.jong.msa.board.common.enums.DBCodeEnum.Group;
import com.jong.msa.board.common.enums.DBCodeEnum.State;
import com.jong.msa.board.common.enums.SortEnum.MemberSort;
import com.jong.msa.board.common.enums.SortEnum.Order;
import com.jong.msa.board.core.validation.annotation.BetweenDate;
import com.jong.msa.board.core.validation.annotation.BetweenDate.DateFields;
import com.jong.msa.board.core.validation.annotation.BetweenDate.DateType;

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
@BetweenDate(checkFields = {
		@DateFields(dateType = DateType.DATE,
				fromField = "createdDateFrom", toField = "createdDateTo", 
				message = "생성일자 검색 시작일은 생성일자 검색 종료일 보다 늦을 수 없습니다."),
		@DateFields(dateType = DateType.DATE,
				fromField = "updatedDateFrom", toField = "updatedDateTo", 
				message = "수정일자 검색 시작일은 수정일자 검색 종료일 보다 늦을 수 없습니다.")	})
public class SearchMemberRequest {

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
	
	@Schema(description = "생성일자 검색 시작일", example = "2024-01-01")
	private LocalDate createdDateFrom;

	@Schema(description = "생성일자 검색 종료일", example = "2024-01-02")
	private LocalDate createdDateTo;

	@Schema(description = "수정일자 검색 시작일", example = "2024-01-01")
	private LocalDate updatedDateFrom;

	@Schema(description = "수정일자 검색 시작일", example = "2024-01-02")
	private LocalDate updatedDateTo;

	@Schema(description = "상태", example = "ACTIVE")
	private State state;

	@Builder.Default
	@Schema(description = "조회 시작 행 번호", example = "0")
	@PositiveOrZero(message = "조회 시작 행 번호는 O 보다 작을 수 없습니다.")
	private long offset = 0;
	
	@Builder.Default
	@Schema(description = "조회 행의 수", example = "10")
	@Positive(message = "조회 행의 수는 1 보다 작을 수 없습니다.")
	private int limit = 10;
	
	@Schema(description = "정렬 필드", example = "CREATED_DATE_TIME")
	private MemberSort sort;
	
	@Schema(description = "정렬 방식", example = "DESC")
	private Order order;

}
