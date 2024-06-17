package com.jong.msa.board.core.web.response;

import java.util.List;

import com.jong.msa.board.common.enums.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

	private ErrorCode errorCode;
	
	@Singular("errorDetails")
	private List<Details> errorDetailsList;
	
	@Getter
	@Builder
	@ToString
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Details {
		
		private String field;
		
		private String message;
		
	}
	
}
