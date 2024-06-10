package com.jong.msa.board.client.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.core.feign.condition.FeignClientCondition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "검색 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = "microservice-search")
public interface SearchFeignClient {

	@Operation(summary = "회원 검색")
	@PostMapping(value = "/apis/search/members",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberListResponse> searchMemberList(
			@RequestBody SearchMemberRequest request);

	@Operation(summary = "게시글 검색")
	@PostMapping(value = "/apis/search/posts",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PostListResponse> searchPostList(
			@RequestBody SearchPostRequest request);

}
