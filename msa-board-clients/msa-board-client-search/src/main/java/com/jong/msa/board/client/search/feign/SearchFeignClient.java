package com.jong.msa.board.client.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jong.msa.board.client.core.condition.FeignClientCondition;
import com.jong.msa.board.client.search.request.MemberSearchRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.constants.MicroserviceNames;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.annotation.APIErrorResponse;
import com.jong.msa.board.core.web.annotation.APIErrorResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "검색 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = MicroserviceNames.SEARCH_MICROSERVICE)
@APIErrorResponses({
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true),
	@APIErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR, errorCode = ErrorCode.UNCHECKED_INTERNAL_ERROR)
})
public interface SearchFeignClient {

	@Operation(summary = "회원 검색")
	@PostMapping(value = "/apis/members/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberListResponse> searchMemberList(
			@RequestBody MemberSearchRequest request);

	@Operation(summary = "게시글 검색")
	@PostMapping(value = "/apis/posts/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PostListResponse> searchPostList(
			@RequestBody PostSearchRequest request);

}
