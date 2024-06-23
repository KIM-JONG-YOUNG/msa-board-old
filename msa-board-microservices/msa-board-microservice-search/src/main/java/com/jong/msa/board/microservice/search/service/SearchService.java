package com.jong.msa.board.microservice.search.service;

import com.jong.msa.board.client.search.request.MemberSearchRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;

public interface SearchService {

	MemberListResponse searchMemberList(MemberSearchRequest request);
	
	PostListResponse searchPostList(PostSearchRequest request);

}
