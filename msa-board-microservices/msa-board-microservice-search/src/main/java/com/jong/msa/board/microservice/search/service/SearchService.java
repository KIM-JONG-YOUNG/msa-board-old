package com.jong.msa.board.microservice.search.service;

import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;

public interface SearchService {

	MemberListResponse searchMemberList(SearchMemberRequest request);
	
	PostListResponse searchPostList(SearchPostRequest request);

}
