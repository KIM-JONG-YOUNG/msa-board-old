package com.jong.msa.board.core.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebFilter(urlPatterns = "/apis/*")
public class LoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		filterChain.doFilter(requestWrapper, responseWrapper);

		log.info(new StringBuilder("Request Info")
				.append("\n - Method	: ").append(requestWrapper.getMethod())
				.append("\n - URL   	: ").append(getURL(requestWrapper))
				.append("\n - Header	: ").append(getHeaderMap(requestWrapper))
				.append("\n - Body	  	: ").append(new String(requestWrapper.getContentAsByteArray()))
				.toString());
		
		log.info(new StringBuilder("Response Info")
				.append("\n - Status	: ").append(responseWrapper.getStatus())
				.append("\n - Header	: ").append(getHeaderMap(responseWrapper))
				.append("\n - Body	  	: ").append(new String(responseWrapper.getContentAsByteArray()))
				.toString());
		
		responseWrapper.copyBodyToResponse();
	}
	
	private String getURL(ContentCachingRequestWrapper request) {
		
		StringBuilder requestURL = new StringBuilder(request.getRequestURI()); 
		String requestQuery = request.getQueryString();
		
		if (StringUtils.hasText(requestQuery)) {
			requestURL.append("?").append(requestQuery);
		}
		
		return requestURL.toString();
	}
	
	private Map<String, List<String>> getHeaderMap(ContentCachingRequestWrapper request) {
		
		Map<String, List<String>> headerMap = new HashMap<>();

		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			
			String headerName = headerNames.nextElement();
			
			if (!headerMap.containsKey(headerName)) {
				headerMap.put(headerName, Collections.list(request.getHeaders(headerName)));
			}
		}
		
		return headerMap;
	}

	private Map<String, List<String>> getHeaderMap(ContentCachingResponseWrapper response) {
		
		Map<String, List<String>> headerMap = new HashMap<>();

		Collection<String> headerNames = response.getHeaderNames();

		headerNames.stream().forEach(headerName -> {
			
			if (!headerMap.containsKey(headerName)) {
				headerMap.put(headerName, new ArrayList<>(response.getHeaders(headerName)));
			}
		});
		
		return headerMap;
	}
	
}
 