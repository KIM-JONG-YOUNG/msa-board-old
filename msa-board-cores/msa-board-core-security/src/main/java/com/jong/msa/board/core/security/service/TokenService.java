package com.jong.msa.board.core.security.service;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.jong.msa.board.common.enums.CodeEnum.Group;

public interface TokenService {

	public String generateAccessToken(UUID id, Group group);
	
	public String generateRefreshToken(UUID id);

	public <T> T validateAccessToken(String accessToken, BiFunction<UUID, Group, T> afterFunction);

	public <T> T validateRefreshToken(String accessToken, Function<UUID, T> afterFunction);

	public void revokeAccessToken(String accessToken);

	public void revokeRefreshToken(String refreshToken);
	
	public void revokeTokenAll(UUID id);

}
