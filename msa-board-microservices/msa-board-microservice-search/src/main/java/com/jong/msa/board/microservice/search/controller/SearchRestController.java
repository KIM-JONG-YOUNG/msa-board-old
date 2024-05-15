package com.jong.msa.board.microservice.search.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.search.feign.SearchFeignClient;
import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.core.validation.utils.BindingResultUtils;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.microservice.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchRestController implements SearchFeignClient {
	
	private final Validator validator;
	
	private final SearchService service;
	
	@Override
	public ResponseEntity<MemberListResponse> searchMemberList(SearchMemberRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);
		
		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult); 
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(service.searchMemberList(request));
		}
	}

	@Override
	public ResponseEntity<PostListResponse> searchPostList(SearchPostRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);
		
		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult); 
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(service.searchPostList(request));
		}
	}

}
