package com.jong.msa.board.microservice.post.service;

import java.util.UUID;

import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;

public interface PostService {

	UUID createPost(CreatePostRequest request);

	void modifyPost(UUID id, ModifyPostRequest request);

	void increasePostViews(UUID id);

	PostDetailsResponse getPost(UUID id);

}
