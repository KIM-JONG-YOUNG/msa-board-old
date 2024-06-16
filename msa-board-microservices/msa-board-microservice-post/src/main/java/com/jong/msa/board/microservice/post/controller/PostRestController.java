package com.jong.msa.board.microservice.post.controller;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.post.feign.PostFeignClient;
import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.client.post.response.PostDetailsResponse.Writer;
import com.jong.msa.board.core.validation.utils.BindingResultUtils;
import com.jong.msa.board.core.web.enums.CommonErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.microservice.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostRestController implements PostFeignClient {

	private final Validator validator;
	
	private final PostService service;
	
	@Override
	public ResponseEntity<Void> createPost(CreatePostRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, CommonErrorCode.INVALID_PARAMETER, bindingResult);

		} else {
			
			return ResponseEntity.status(HttpStatus.CREATED)
					.header(HttpHeaders.LOCATION, new StringBuilder()
							.append("/apis/posts/").append(service.createPost(request))
							.toString())
					.build();
		}
	}

	@Override
	public ResponseEntity<Void> modifyPost(UUID id, ModifyPostRequest request) {

		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, CommonErrorCode.INVALID_PARAMETER, bindingResult);

		} else {
			
			service.modifyPost(id, request);
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.header(HttpHeaders.LOCATION, new StringBuilder()
							.append("/apis/posts/").append(id)
							.toString())
					.build();
		}
	}

	@Override
	public ResponseEntity<Void> increasePostViews(UUID id) {

		service.increasePostViews(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.header(HttpHeaders.LOCATION, new StringBuilder()
						.append("/apis/posts/").append(id)
						.toString())
				.build();
	}

	@Override
	public ResponseEntity<Writer> getPostWriter(UUID id) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(service.getPost(id).getWriter());
	}

	@Override
	public ResponseEntity<PostDetailsResponse> getPost(UUID id) {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.getPost(id));
	}

}
