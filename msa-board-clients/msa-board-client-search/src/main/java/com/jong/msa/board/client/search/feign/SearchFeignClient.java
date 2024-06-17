package com.jong.msa.board.client.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.jong.msa.board.client.core.condition.FeignClientCondition;
import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "검색 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = "microservice-search")
public interface SearchFeignClient {

	@Operation(summary = "회원 검색")
	@GetMapping(value = "/apis/search/members",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberListResponse> searchMemberList(
			@SpringQueryMap SearchMemberRequest request);

	@Operation(summary = "게시글 검색")
	@GetMapping(value = "/apis/search/posts",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PostListResponse> searchPostList(
			@SpringQueryMap SearchPostRequest request);

}
