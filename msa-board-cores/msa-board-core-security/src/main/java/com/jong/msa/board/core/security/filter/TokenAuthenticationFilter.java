package com.jong.msa.board.core.security.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jong.msa.board.core.security.service.TokenService;
import com.jong.msa.board.core.security.service.TokenService.TokenValidResult;

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

			TokenValidResult validResult = tokenService.validateAccessToken(accessToken);
			
			if (validResult.isValid()) {

				String roleName = new StringBuilder("ROLE_").append(validResult.getGroup()).toString();
						
				GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

				Authentication authentication = new UsernamePasswordAuthenticationToken(validResult.getId(), null, Arrays.asList(authority));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} else {
				request.setAttribute("tokenErrorCode", validResult.getErrorCode());
			}
		}

		filterChain.doFilter(request, response);
	}

}
