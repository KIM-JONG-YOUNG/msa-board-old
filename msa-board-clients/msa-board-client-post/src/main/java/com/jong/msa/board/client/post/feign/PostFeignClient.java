package com.jong.msa.board.client.post.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jong.msa.board.client.core.condition.FeignClientCondition;
import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.common.constants.MicroserviceNames;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.annotation.APIErrorResponse;
import com.jong.msa.board.core.web.annotation.APIErrorResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시글 관리 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = MicroserviceNames.POST_MICROSERVICE)
@APIErrorResponses({
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true),
	@APIErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR, errorCode = ErrorCode.UNCHECKED_INTERNAL_ERROR)
})
public interface PostFeignClient {

	@Operation(summary = "게시글 생성")
	@PostMapping(value = "/apis/posts",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST_WRITER),
		@APIErrorResponse(status = HttpStatus.BAD_GATEWAY, errorCode = ErrorCode.UNCHECKED_EXTERNAL_ERROR)
	})
	ResponseEntity<Void> createPost(
			@RequestBody PostCreateRequest request);

	@Operation(summary = "게시글 수정")
	@PatchMapping(value = "/apis/posts/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	})
	ResponseEntity<Void> modifyPost(
			@PathVariable(name = "id") UUID id,
			@RequestBody PostModifyRequest request);

	@Operation(summary = "게시글 조회수 증가")
	@PatchMapping(value = "/apis/posts/{id}/views/increase")
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	})
	ResponseEntity<Void> increasePostViews(
			@PathVariable(name = "id") UUID id);

	@Operation(summary = "게시글 작성자 조회")
	@GetMapping(value = "/apis/posts/{id}/writer")
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST_WRITER),
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	})
	ResponseEntity<PostDetailsResponse.Writer> getPostWriter(
			@PathVariable(name = "id") UUID id);

	@Operation(summary = "게시글 조회")
	@GetMapping(value = "/apis/posts/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST_WRITER),
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST),
		@APIErrorResponse(status = HttpStatus.BAD_GATEWAY, errorCode = ErrorCode.UNCHECKED_EXTERNAL_ERROR)
	})
	ResponseEntity<PostDetailsResponse> getPost(
			@PathVariable(name = "id") UUID id);

}
