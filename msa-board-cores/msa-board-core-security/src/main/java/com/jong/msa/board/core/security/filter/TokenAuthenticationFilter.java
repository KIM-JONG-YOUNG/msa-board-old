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

import com.jong.msa.board.common.enums.CodeEnum.ErrorCode;
import com.jong.msa.board.core.security.exception.RevokedJwtException;
import com.jong.msa.board.core.security.service.TokenService;

import io.jsonwebtoken.ExpiredJwtException;
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

			try {

				Authentication authentication = tokenService.validateAccessToken(accessToken, (id, group) -> {

					String roleName = new StringBuilder("ROLE_").append(group).toString();
					
					GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

					return new UsernamePasswordAuthenticationToken(id, null, Arrays.asList(authority));
				});

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (ExpiredJwtException e) {
				request.setAttribute("tokenErrorCode", ErrorCode.EXPIRED_ACCESS_TOKEN);
			} catch (RevokedJwtException e) {
				request.setAttribute("tokenErrorCode", ErrorCode.REVOKED_ACCESS_TOKEN);
			} catch (Exception e) {
				request.setAttribute("tokenErrorCode", ErrorCode.INVALID_ACCESS_TOKEN);
			}
		} else {
			request.setAttribute("tokenErrorCode", ErrorCode.UNAUTHORIZED_MEMBER);
		}

		filterChain.doFilter(request, response);
	}

}
