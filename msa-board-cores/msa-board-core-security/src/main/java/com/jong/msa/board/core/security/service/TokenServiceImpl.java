package com.jong.msa.board.core.security.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.Group;
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
		long cachingTime = Math.max(redisTemplate.getExpire(cachingKey, TimeUnit.SECONDS), expireSeconds);
		
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
	public Map.Entry<UUID, Group> validateAccessToken(String accessToken) {
		
		try {

			Claims claims = Jwts.parser()
					.setSigningKey(properties.getAccessToken().getSecretKey())
					.parseClaimsJws(accessToken)
					.getBody();
			
			UUID id = UUID.fromString(claims.get("id", String.class));
			Group group = Group.valueOf(claims.get("group", String.class));
			
			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

			if (redisTemplate.opsForSet().isMember(cachingKey, accessToken)) {
				
				return new AbstractMap.SimpleEntry<>(id, group);
				
			} else {
				
				throw new RevokedJwtException("사용할 수 없는 Access Token 입니다.");
			}
		} catch (ExpiredJwtException e) {
			
			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 Access Token 입니다.", e);
			
		} catch (Exception e) {
			
			throw new MalformedJwtException("유효하지 않은 Access Token 입니다.", e);
		}
	}

	@Override
	public UUID validateRefreshToken(String refreshToken) {
		
		try {

			Claims claims = Jwts.parser()
					.setSigningKey(properties.getAccessToken().getSecretKey())
					.parseClaimsJws(refreshToken)
					.getBody();
			
			UUID id = UUID.fromString(claims.get("id", String.class));
			
			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

			if (redisTemplate.opsForSet().isMember(cachingKey, refreshToken)) {
				
				return id;
				
			} else {
				
				throw new RevokedJwtException("사용할 수 없는 Access Token 입니다.");
			}
		} catch (ExpiredJwtException e) {
			
			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 Access Token 입니다.", e);
			
		} catch (Exception e) {
			
			throw new MalformedJwtException("유효하지 않은 Access Token 입니다.", e);
		}
	}

	@Override
	public void revokeAccessToken(String accessToken) {

		try {

			UUID id = validateAccessToken(accessToken).getKey();
			
			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

			redisTemplate.opsForSet().remove(cachingKey, accessToken);

		} catch (JwtException e) {
			
			log.warn(e.getMessage());
		}
	}

	@Override
	public void revokeRefreshToken(String refreshToken) {

		try {

			UUID id = validateRefreshToken(refreshToken);

			String cachingKey = new StringBuilder(RedisKeyPrefixes.TOKENS_KEY).append(id).toString(); 

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
