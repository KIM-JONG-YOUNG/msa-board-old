package com.jong.msa.board.client.search.request.param;

import java.time.LocalDate;

import com.jong.msa.board.common.constants.Patterns;
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
				fromField = "from", toField = "to", 
				message = "시작일자는 종료일자 보다 늦을 수 없습니다.")
		})
public class DateRange {

	@Schema(description = "시작일자", example = Patterns.DATE_FORMAT)
	private LocalDate from;
	
	@Schema(description = "종료일자", example = Patterns.DATE_FORMAT)
	private LocalDate to;
	
}
