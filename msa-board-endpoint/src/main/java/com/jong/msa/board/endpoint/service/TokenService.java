package com.jong.msa.board.endpoint.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jong.msa.board.common.enums.DBCodeEnum.Group;
import com.jong.msa.board.common.enums.ErrorCodeEnum.TokenErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public interface TokenService {

	public TokenGenerateResult generateAccessToken(UUID id, Group group);
	
	public TokenGenerateResult generateRefreshToken(UUID id);

	public TokenValidResult validateAccessToken(String accessToken);

	public TokenValidResult validateRefreshToken(String refreshToken);

	public void revokeAccessToken(String accessToken);

	public void revokeRefreshToken(String refreshToken);
	
	public void revokeTokenAll(UUID id);

	@Getter
	@Builder
	@ToString 
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TokenGenerateResult {

		private String token;
		
		private LocalDateTime expiredTime;

	}
	
	@Getter
	@Builder
	@ToString 
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TokenValidResult {

		private UUID id;
		
		private Group group;

		private TokenErrorCode errorCode;

		public boolean isValid() {
			
			return errorCode == null && id != null;
		}
		
	}

}
