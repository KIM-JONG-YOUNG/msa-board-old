package com.jong.msa.board.core.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.core.security.service.TokenService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String accessToken = request.getHeader("Access-Token");

		if (StringUtils.hasText(accessToken)) {

			Map.Entry<UUID, Group> idAndGroup = tokenService.validateAccessToken(accessToken);
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(idAndGroup.getKey(), null, Arrays.asList(
					new SimpleGrantedAuthority(new StringBuilder("ROLE_").append(idAndGroup.getValue()).toString())));

			SecurityContextHolder.getContext().setAuthentication(authentication);

		}

		filterChain.doFilter(request, response);
	}

}
