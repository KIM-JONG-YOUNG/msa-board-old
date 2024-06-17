package com.jong.msa.board.core.security.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jong.msa.board.core.security.filter.TokenAuthenticationErrorFilter;
import com.jong.msa.board.core.security.filter.TokenAuthenticationFilter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityConfig.TokenProperties.class)
@ImportAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class SecurityConfig {

	private final TokenAuthenticationFilter tokenAuthenticationFilter; 

	private final TokenAuthenticationErrorFilter tokenAuthenticationErrorFilter; 

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(requests -> requests.anyRequest().permitAll())
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenAuthenticationErrorFilter, TokenAuthenticationFilter.class)
                .build();
	}
	
	@Getter
	@ToString
	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("jwt")
	public static class TokenProperties {

		private final Details accessToken;
		
		private final Details refreshToken;

		@Getter
		@ToString
		@ConstructorBinding
		@RequiredArgsConstructor
		public static class Details {

			private final String secretKey;
		
			private final long expireSeconds;
			
		}
	}

}
