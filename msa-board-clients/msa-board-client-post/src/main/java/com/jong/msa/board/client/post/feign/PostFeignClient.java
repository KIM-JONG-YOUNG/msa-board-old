package com.jong.msa.board.client.post.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.core.feign.condition.FeignClientCondition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시글 관리 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = "microservice-post")
public interface PostFeignClient {

	@Operation(summary = "게시글 생성")
	@PostMapping(value = "/apis/posts",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> createPost(
			@RequestBody CreatePostRequest request);

	@Operation(summary = "게시글 수정")
	@PatchMapping(value = "/apis/posts/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyPost(
			@PathVariable(name = "id") UUID id,
			@RequestBody ModifyPostRequest request);

	@Operation(summary = "게시글 조회수 증가")
	@PatchMapping(value = "/apis/posts/{id}/views/increase")
	ResponseEntity<Void> increasePostViews(
			@PathVariable(name = "id") UUID id);

	@Operation(summary = "게시글 작성자 조회")
	@GetMapping(value = "/apis/posts/{id}/writer")
	ResponseEntity<PostDetailsResponse.Writer> getPostWriter(
			@PathVariable(name = "id") UUID id);

	@Operation(summary = "게시글 조회")
	@GetMapping(value = "/apis/posts/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PostDetailsResponse> getPost(
			@PathVariable(name = "id") UUID id);

}
