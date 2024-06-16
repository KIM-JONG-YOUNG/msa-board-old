package com.jong.msa.board.core.security.service;

import java.util.Map;
import java.util.UUID;

import com.jong.msa.board.common.enums.Group;

public interface TokenService {

	public String generateAccessToken(UUID id, Group group);
	
	public String generateRefreshToken(UUID id);

	public Map.Entry<UUID, Group> validateAccessToken(String accessToken);

	public UUID validateRefreshToken(String refreshToken);

	public void revokeAccessToken(String accessToken);

	public void revokeRefreshToken(String refreshToken);
	
	public void revokeTokenAll(UUID id);

}



