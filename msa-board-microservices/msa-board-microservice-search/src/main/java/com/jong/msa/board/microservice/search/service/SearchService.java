package com.jong.msa.board.microservice.search.service;

import com.jong.msa.board.client.search.request.PagingRequest;
import com.jong.msa.board.client.search.request.param.MemberCondition;
import com.jong.msa.board.client.search.request.param.PostCondition;
import com.jong.msa.board.client.search.response.PagingListResponse;
import com.jong.msa.board.client.search.response.result.MemberItem;
import com.jong.msa.board.client.search.response.result.PostItem;

public interface SearchService {

//	MemberListResponse searchMemberList(SearchMemberRequest request);
//	
//	PostListResponse searchPostList(SearchPostRequest request);

	PagingListResponse<MemberItem> searchMemberList(PagingRequest<MemberCondition> request);
	
	PagingListResponse<PostItem> searchPostList(PagingRequest<PostCondition> request);

}
