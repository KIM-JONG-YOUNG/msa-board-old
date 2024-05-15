package com.jong.msa.board.endpoint.utils;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContextUtils {

	public static UUID getAuthenticationId() {
		
		try {
			return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
	
}
