package com.jong.msa.board.microservice.post.service;

import java.util.UUID;

import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;

public interface PostService {

	UUID createPost(PostCreateRequest request);

	void modifyPost(UUID id, PostModifyRequest request);

	void increasePostViews(UUID id);

	PostDetailsResponse getPost(UUID id);

}
