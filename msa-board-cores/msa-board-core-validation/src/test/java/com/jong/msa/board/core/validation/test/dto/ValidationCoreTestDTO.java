package com.jong.msa.board.core.validation.test.dto;

import com.jong.msa.board.core.validation.annotation.StringValidate;
import com.jong.msa.board.core.validation.annotation.StringValidate.BlankCheck;
import com.jong.msa.board.core.validation.annotation.StringValidate.PatternCheck;
import com.jong.msa.board.core.validation.annotation.StringValidate.SizeCheck;

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
public class ValidationCoreTestDTO {
	
	@StringValidate(
			blankCheck = @BlankCheck(message = "값이 비어있을 수 없습니다."))
	private String blankCheckField;

	@StringValidate(
			blankCheck = @BlankCheck(nullable = true, message = "값이 비어있을 수 없습니다."))
	private String nullableBlankCheckField;

	@StringValidate(
			sizeCheck = @SizeCheck(message = "10자를 초과할 수 없습니다."))
	private String sizeCheckField;

	@StringValidate(
			patternCheck = @PatternCheck(regexp = "[A-Z]", message = "A-Z만 사용 가능합니다."))
	private String patternCheckField;

}
