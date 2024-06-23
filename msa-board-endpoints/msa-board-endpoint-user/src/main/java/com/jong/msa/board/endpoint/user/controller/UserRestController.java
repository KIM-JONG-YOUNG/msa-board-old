package com.jong.msa.board.endpoint.user.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.member.feign.MemberFeignClient;
import com.jong.msa.board.client.member.request.MemberCreateRequest;
import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.feign.PostFeignClient;
import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.client.search.feign.SearchFeignClient;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.common.enums.State;
import com.jong.msa.board.core.security.exception.RevokedJwtException;
import com.jong.msa.board.core.security.service.TokenService;
import com.jong.msa.board.core.security.utils.SecurityContextUtils;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.endpoint.user.mapper.UserRequestMapper;
import com.jong.msa.board.endpoint.user.request.UserJoinRequest;
import com.jong.msa.board.endpoint.user.request.UserLoginRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPasswordModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostSearchRequest;
import com.jong.msa.board.endpoint.user.request.UserPostWriteRequest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestController implements UserOperations {

	private final UserRequestMapper requestMapper;
	
	private final MemberFeignClient memberFeignClient;
	
	private final PostFeignClient postFeignClient;
	
	private final SearchFeignClient searchFeignClient;

	private final TokenService tokenService;

	@Override
	public ResponseEntity<Void> joinUser(UserJoinRequest request) {

		MemberCreateRequest createRequest = requestMapper.toRequest(request);

		return memberFeignClient.createMember(createRequest);
	}

	@Override
	public ResponseEntity<Void> loginUser(UserLoginRequest request) {
		
		MemberLoginRequest loginRequest = requestMapper.toRequest(request);

		MemberDetailsResponse member = memberFeignClient.loginMember(loginRequest).getBody();

		if (member.getGroup() != Group.USER) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_USER_USERNAME);
			
		} else {
		
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.header("Access-Token", tokenService.generateAccessToken(member.getId(), member.getGroup()))
					.header("Refresh-Token", tokenService.generateRefreshToken(member.getId()))
					.build();
		}
	}

	@Override
	public ResponseEntity<Void> logoutUser(String accessToken) {
		
		tokenService.revokeAccessToken(accessToken);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	public ResponseEntity<Void> refreshUser(String refreshToken) {
		
		try {

			UUID id = tokenService.validateRefreshToken(refreshToken);
			
			MemberDetailsResponse member = memberFeignClient.getMember(id).getBody();

			if (member.getGroup() == Group.USER) {

				tokenService.revokeRefreshToken(refreshToken);

				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.header("Access-Token", tokenService.generateAccessToken(member.getId(), member.getGroup()))
						.header("Refresh-Token", tokenService.generateRefreshToken(member.getId()))
						.build();
			} else {

				throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.REVOKED_REFRESH_TOKEN);
			}
		} catch (ExpiredJwtException e) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.EXPIRED_ACCESS_TOKEN);
			
		} catch (RevokedJwtException e) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.REVOKED_REFRESH_TOKEN);
			
		} catch (JwtException e) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REFRESH_TOKEN);
		}
	}

	@Override
	public ResponseEntity<Void> modifyUser(UserModifyRequest request) {
		
		MemberModifyRequest modifyRequest = requestMapper.toRequest(request);

		UUID id = SecurityContextUtils.getAuthenticationId();

		return memberFeignClient.modifyMember(id, modifyRequest);
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseEntity<Void> modifyUserPassword(UserPasswordModifyRequest request) {

		MemberPasswordModifyRequest modifyPasswordRequest = requestMapper.toRequest(request);

		UUID id = SecurityContextUtils.getAuthenticationId();
		
		ResponseEntity<Void> response = memberFeignClient.modifyMemberPassword(id, modifyPasswordRequest);
		
		tokenService.revokeTokenAll(id);
		
		return response;
	}

	@Override
	public ResponseEntity<MemberDetailsResponse> getUser() {

		return memberFeignClient.getMember(SecurityContextUtils.getAuthenticationId());
	}

	@Override
	public ResponseEntity<Void> writePost(UserPostWriteRequest request) {

		UUID id = SecurityContextUtils.getAuthenticationId();
		
		PostCreateRequest createRequest = requestMapper.toRequest(request, id);

		return postFeignClient.createPost(createRequest);					
	}

	@Override
	public ResponseEntity<Void> modifyPost(UUID postId, UserPostModifyRequest request) {
		
		PostModifyRequest modifyRequest = requestMapper.toRequest(request);

		UUID id = SecurityContextUtils.getAuthenticationId();

		PostDetailsResponse post = postFeignClient.getPost(postId).getBody();
		
		if (post.getState() == State.DEACTIVE) {
			
			throw new RestServiceException(HttpStatus.GONE, ErrorCode.DEACTIVE_POST);
			
		} else if (!post.getWriter().getId().equals(id)) {
			
			throw new RestServiceException(HttpStatus.FORBIDDEN, ErrorCode.NOT_POST_WRITER);
			
		} else {
			
			return postFeignClient.modifyPost(postId, modifyRequest);
		}
	}

	@Override
	public ResponseEntity<Void> removePost(UUID postId) {
		
		UUID id = SecurityContextUtils.getAuthenticationId();

		PostDetailsResponse.Writer writer = postFeignClient.getPostWriter(postId).getBody();
		
		if (writer.getId().equals(id)) {
		
			return postFeignClient.modifyPost(postId, PostModifyRequest.builder().state(State.DEACTIVE).build());
			
		} else {
			
			throw new RestServiceException(HttpStatus.FORBIDDEN, ErrorCode.NOT_POST_WRITER);
		}
	}

	@Override
	public ResponseEntity<PostDetailsResponse> getPost(UUID postId) {

		PostDetailsResponse post = postFeignClient.getPost(postId).getBody();

		if (post.getState() == State.DEACTIVE) {
			
			throw new RestServiceException(HttpStatus.GONE, ErrorCode.DEACTIVE_POST);

		} else {
			
			postFeignClient.increasePostViews(postId);
			
			return ResponseEntity.status(HttpStatus.OK).body(post);			
		}
	}

	@Override
	public ResponseEntity<PostListResponse> searchPostList(UserPostSearchRequest request) {
		
		PostSearchRequest searchRequest = requestMapper.toRequest(request);

		return searchFeignClient.searchPostList(searchRequest);
	}

}
