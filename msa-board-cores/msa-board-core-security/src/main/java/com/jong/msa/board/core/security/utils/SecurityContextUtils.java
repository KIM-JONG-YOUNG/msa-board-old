package com.jong.msa.board.core.security.utils;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContextUtils {

	public static UUID getAuthenticationId() {
		
		try {
			
			return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
			
		} catch (Exception e) {
			
			return null;
		}
	}
	
}
