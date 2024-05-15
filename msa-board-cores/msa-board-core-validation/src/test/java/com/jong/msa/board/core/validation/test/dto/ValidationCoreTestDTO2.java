package com.jong.msa.board.core.validation.test.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.jong.msa.board.core.validation.annotation.BetweenDate;
import com.jong.msa.board.core.validation.annotation.BetweenDate.DateFields;
import com.jong.msa.board.core.validation.annotation.BetweenDate.DateType;

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
		@DateFields(dateType = DateType.TIME,
				fromField = "fromTime", toField = "toTime", 
				message = "시작시간은 종료시간 이후 일 수 없습니다."),
		@DateFields(dateType = DateType.DATE,
				fromField = "fromDate", toField = "toDate", 
				message = "시작일자는 종료일자 이후 일 수 없습니다."),
		@DateFields(dateType = DateType.DATE_TIME,
				fromField = "fromDateTime", toField = "toDateTime", 
				message = "시작일시는 종료일시 이후 일 수 없습니다.")
})
public class ValidationCoreTestDTO2 {

	private LocalTime fromTime;
	
	private LocalTime toTime;

	private LocalDate fromDate;
	
	private LocalDate toDate;

	private LocalDateTime fromDateTime;
	
	private LocalDateTime toDateTime;

}
