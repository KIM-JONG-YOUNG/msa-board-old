package com.jong.msa.board.endpoint.request;

import com.jong.msa.board.common.enums.DBCodeEnum.Gender;
import com.jong.msa.board.core.validation.annotation.StringValidate;
import com.jong.msa.board.core.validation.annotation.StringValidate.BlankCheck;
import com.jong.msa.board.core.validation.annotation.StringValidate.PatternCheck;
import com.jong.msa.board.core.validation.annotation.StringValidate.SizeCheck;

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
public class UserModifyRequest {

	@Schema(description = "이름", example = "name")
	@StringValidate(
			blankCheck = @BlankCheck(nullable = true, message = "이름은 비어있을 수 없습니다."),
			sizeCheck = @SizeCheck(max = 30, message = "이름은 30자를 초과할 수 없습니다."))
	private String name;
	
	@Schema(description = "성별", example = "MAIL")
	private Gender gender;
	
	@Schema(description = "이메일", example = "test@example.com")
	@StringValidate(
			blankCheck = @BlankCheck(nullable = true, message = "이메일은 비어있을 수 없습니다."),
			sizeCheck = @SizeCheck(max = 60, message = "이메일은 60자를 초과할 수 없습니다."),
			patternCheck = @PatternCheck(
					regexp = "^[0-9a-zA-Z-_.]+@[0-9a-zA-Z-_.]+.[a-zA-Z]{2,3}$", 
					message = "이메일이 형식에 맞지 않습니다."))
	private String email;

}
