package com.jong.msa.board.core.security.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.CodeEnum.Group;
import com.jong.msa.board.core.security.config.SecurityConfig.TokenProperties;
import com.jong.msa.board.core.security.exception.RevokedJwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final TokenProperties properties;

	private final RedisTemplate<String, String> redisTemplate;

	private String generateToken(UUID id, Group group, String secretKey, long expireSeconds) {

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
		
		return token;
	}
	
	@Override
	public String generateAccessToken(UUID id, Group group) {
		
		return generateToken(id, group, 
				properties.getAccessToken().getSecretKey(), 
				properties.getAccessToken().getExpireSeconds());	
	}

	@Override
	public String generateRefreshToken(UUID id) {

		return generateToken(id, null, 
				properties.getRefreshToken().getSecretKey(), 
				properties.getRefreshToken().getExpireSeconds());	
	}

	@Override
	public <T> T validateAccessToken(String accessToken, BiFunction<UUID, Group, T> afterFunction) {
		
		UUID id = null;
		Group group = null;

		try {

			Claims claims = Jwts.parser()
					.setSigningKey(properties.getAccessToken().getSecretKey())
					.parseClaimsJws(accessToken)
					.getBody();
			
			id = UUID.fromString(claims.get("id", String.class));
			group = Group.valueOf(claims.get("group", String.class));
			
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 Access Token 입니다.", e);
		} catch (Exception e) {
			throw new MalformedJwtException("유효하지 않은 Access Token 입니다.", e);
		}

		String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

		if (!redisTemplate.opsForSet().isMember(cachingKey, accessToken)) {
			throw new RevokedJwtException("사용할 수 없는 Access Token 입니다.");
		} else {
			return afterFunction.apply(id, group);
		}
	}

	@Override
	public <T> T validateRefreshToken(String accessToken, Function<UUID, T> afterFunction) {
		
		UUID id = null;

		try {

			Claims claims = Jwts.parser()
					.setSigningKey(properties.getAccessToken().getSecretKey())
					.parseClaimsJws(accessToken)
					.getBody();
			
			id = UUID.fromString(claims.get("id", String.class));
			
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 Refresh Token 입니다.", e);
		} catch (Exception e) {
			throw new MalformedJwtException("유효하지 않은 Refresh Token 입니다.", e);
		}

		String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

		if (!redisTemplate.opsForSet().isMember(cachingKey, accessToken)) {
			throw new RevokedJwtException("사용할 수 없는 Refresh Token 입니다.");
		} else {
			return afterFunction.apply(id);
		}
	}

	@Override
	public void revokeAccessToken(String accessToken) {

		try {

			String cachingKey = validateAccessToken(accessToken,
					(id, group) -> new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString());

			redisTemplate.opsForSet().remove(cachingKey, accessToken);

		} catch (JwtException e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public void revokeRefreshToken(String refreshToken) {

		try {

			String cachingKey = validateRefreshToken(refreshToken,
					id -> new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString());

			redisTemplate.opsForSet().remove(cachingKey, refreshToken);

		} catch (JwtException e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public void revokeTokenAll(UUID id) {

		redisTemplate.delete(new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString());
	}

}
