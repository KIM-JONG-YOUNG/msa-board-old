package com.jong.msa.board.microservice.search.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.search.feign.SearchFeignClient;
import com.jong.msa.board.client.search.request.MemberSearchRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.microservice.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchRestController implements SearchFeignClient {

	private final SearchService service;

	@Override
	public ResponseEntity<MemberListResponse> searchMemberList(MemberSearchRequest request) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(service.searchMemberList(request));
	}

	@Override
	public ResponseEntity<PostListResponse> searchPostList(PostSearchRequest request) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(service.searchPostList(request));
	}

}
