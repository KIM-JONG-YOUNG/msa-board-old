package com.jong.msa.board.core.security.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.DBCodeEnum.Group;
import com.jong.msa.board.common.enums.ErrorCodeEnum.TokenErrorCode;
import com.jong.msa.board.core.security.config.SecurityConfig.TokenProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final TokenProperties properties;

	private final RedisTemplate<String, String> redisTemplate;

	private TokenGenerateResult generateToken(UUID id, Group group, String secretKey, long expireSeconds) {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expireTime = now.plusSeconds(expireSeconds);
		
		String token = Jwts.builder()
				.setId(UUID.randomUUID().toString())
				.claim("id", id)
				.claim("group", group)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.setExpiration(Timestamp.valueOf(expireTime))
				.compact();

		String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 
		long cachingTime = Math.max(
				redisTemplate.getExpire(cachingKey, TimeUnit.SECONDS), 
				properties.getAccessToken().getExpireSeconds());
		
		redisTemplate.opsForSet().add(cachingKey, token);
		redisTemplate.expire(cachingKey, cachingTime, TimeUnit.SECONDS);
		
		return TokenGenerateResult.builder()
				.token(token)
				.expiredTime(expireTime)
				.build();
	}
	
	@Override
	public TokenGenerateResult generateAccessToken(UUID id, Group group) {
		
		return generateToken(id, group, 
				properties.getAccessToken().getSecretKey(), 
				properties.getAccessToken().getExpireSeconds());	
	}

	@Override
	public TokenGenerateResult generateRefreshToken(UUID id) {

		return generateToken(id, null, 
				properties.getRefreshToken().getSecretKey(), 
				properties.getRefreshToken().getExpireSeconds());	
	}

	@Override
	public TokenValidResult validateAccessToken(String accessToken) {
		
		try {

			Claims claims = Jwts.parser()
					.setSigningKey(properties.getAccessToken().getSecretKey())
					.parseClaimsJws(accessToken)
					.getBody();
			
			UUID id = UUID.fromString(claims.get("id", String.class));
			Group group = Group.valueOf(claims.get("group", String.class));
			
			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

			if (redisTemplate.opsForSet().isMember(cachingKey, accessToken)) {
				return TokenValidResult.builder().id(id).group(group).build();
			} else {
				return TokenValidResult.builder().errorCode(TokenErrorCode.REVOKED_ACCESS_TOKEN).build();
			}
		} catch (ExpiredJwtException e) {
			return TokenValidResult.builder().errorCode(TokenErrorCode.EXPIRED_ACCESS_TOKEN).build();
		} catch (Exception e) {
			return TokenValidResult.builder().errorCode(TokenErrorCode.INVALID_ACCESS_TOKEN).build();
		}
	}

	@Override
	public TokenValidResult validateRefreshToken(String refreshToken) {
		
		try {

			Claims claims = Jwts.parser()
					.setSigningKey(properties.getRefreshToken().getSecretKey())
					.parseClaimsJws(refreshToken)
					.getBody();
			
			UUID id = UUID.fromString(claims.get("id", String.class));
			
			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

			if (redisTemplate.opsForSet().isMember(cachingKey, refreshToken)) {
				return TokenValidResult.builder().id(id).build();
			} else {
				return TokenValidResult.builder().errorCode(TokenErrorCode.REVOKED_REFRESH_TOKEN).build();
			}
		} catch (ExpiredJwtException e) {
			return TokenValidResult.builder().errorCode(TokenErrorCode.EXPIRED_REFRESH_TOKEN).build();
		} catch (Exception e) {
			return TokenValidResult.builder().errorCode(TokenErrorCode.INVALID_REFRESH_TOKEN).build();
		}
	}

	@Override
	public void revokeAccessToken(String accessToken) {

		TokenValidResult validResult = validateAccessToken(accessToken);
		
		if (validResult.isValid()) {
			
			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(validResult.getId()).toString();

			redisTemplate.opsForSet().remove(cachingKey, accessToken);
		}
	}

	@Override
	public void revokeRefreshToken(String refreshToken) {

		TokenValidResult validResult = validateRefreshToken(refreshToken);
		
		if (validResult.isValid()) {
			
			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(validResult.getId()).toString();

			redisTemplate.opsForSet().remove(cachingKey, refreshToken);
		}
	}

	@Override
	public void revokeTokenAll(UUID id) {

		redisTemplate.delete(new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString());
	}

}
