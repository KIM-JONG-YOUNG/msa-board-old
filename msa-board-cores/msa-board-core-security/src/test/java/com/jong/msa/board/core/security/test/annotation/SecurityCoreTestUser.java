package com.jong.msa.board.core.security.test.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.jong.msa.board.common.enums.CodeEnum.Group;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = SecurityCoreTestUser.ContextFactory.class)
public @interface SecurityCoreTestUser {

	String id();
		
	Group[] groups();
	
	public static class ContextFactory implements WithSecurityContextFactory<SecurityCoreTestUser> {

		@Override
		public SecurityContext createSecurityContext(SecurityCoreTestUser annotation) {
			
			List<GrantedAuthority> authorityList = Stream.of(annotation.groups())
				.map(Group::name)
				.map(String::toUpperCase)
				.map(x -> new SimpleGrantedAuthority("ROLE_" + x))
				.collect(Collectors.toList());
			
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			
			context.setAuthentication(new UsernamePasswordAuthenticationToken(annotation.id(), null, authorityList));
			
			return context;
		}

	}
	
}
