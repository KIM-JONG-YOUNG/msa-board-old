package com.jong.msa.board.endpoint.response;

import java.time.LocalDateTime;

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
public class TokenSetResponse {

	@Schema(description = "Access Token")
	private String accessToken;

	@Schema(description = "Refresh Token")
	private String refreshToken;

	@Schema(description = "Access Token 만료일시")
	private LocalDateTime accessTokenExpiredDateTime;

	@Schema(description = "Refresh Token 만료일시")
	private LocalDateTime refreshTokenExpiredDateTime;

}
