package com.jong.msa.board.client.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jong.msa.board.client.search.request.PagingRequest;
import com.jong.msa.board.client.search.request.param.MemberCondition;
import com.jong.msa.board.client.search.request.param.PostCondition;
import com.jong.msa.board.client.search.response.PagingListResponse;
import com.jong.msa.board.client.search.response.result.MemberItem;
import com.jong.msa.board.client.search.response.result.PostItem;
import com.jong.msa.board.core.feign.condition.FeignClientCondition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "검색 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = "microservice-search")
public interface SearchFeignClient {

	@Operation(summary = "회원 검색")
	@PostMapping(value = "/apis/members/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PagingListResponse<MemberItem>> searchMemberList(
			@RequestBody PagingRequest<MemberCondition> request);
	
	@Operation(summary = "게시글 검색")
	@PostMapping(value = "/apis/posts/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PagingListResponse<PostItem>> searchPostList(
			@RequestBody PagingRequest<PostCondition> request);

}
