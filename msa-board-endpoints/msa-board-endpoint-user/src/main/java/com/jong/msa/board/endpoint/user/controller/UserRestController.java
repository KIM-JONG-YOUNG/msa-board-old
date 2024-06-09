package com.jong.msa.board.endpoint.user.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.member.feign.MemberFeignClient;
import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.feign.PostFeignClient;
import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.client.search.feign.SearchFeignClient;
import com.jong.msa.board.client.search.request.PagingRequest;
import com.jong.msa.board.client.search.request.param.PostCondition;
import com.jong.msa.board.client.search.response.PagingListResponse;
import com.jong.msa.board.client.search.response.result.PostItem;
import com.jong.msa.board.common.enums.CodeEnum.Group;
import com.jong.msa.board.common.enums.CodeEnum.State;
import com.jong.msa.board.core.security.exception.RevokedJwtException;
import com.jong.msa.board.core.security.service.TokenService;
import com.jong.msa.board.core.security.utils.SecurityContextUtils;
import com.jong.msa.board.core.validation.utils.BindingResultUtils;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.endpoint.user.exception.UserServiceException;
import com.jong.msa.board.endpoint.user.mapper.UserRequestMapper;
import com.jong.msa.board.endpoint.user.request.UserJoinMemberRequest;
import com.jong.msa.board.endpoint.user.request.UserLoginRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyPasswordRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyPostRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserWritePostRequest;
import com.jong.msa.board.endpoint.user.request.param.UserPostCondition;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestController implements UserOperations {

	private final Validator validator;
	
	private final UserRequestMapper requestMapper;
	
	private final MemberFeignClient memberFeignClient;
	
	private final PostFeignClient postFeignClient;
	
	private final SearchFeignClient searchFeignClient;

	private final TokenService tokenService;

	@Override
	public ResponseEntity<Void> joinUser(UserJoinMemberRequest request) {

		BindingResult bindingResult = BindingResultUtils.validate(request, validator);
		
		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
			
			CreateMemberRequest createRequest = requestMapper.toRequest(request);
			
			bindingResult = BindingResultUtils.validate(createRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {
				return memberFeignClient.createMember(createRequest);
			}
		}
	}

	@Override
	public ResponseEntity<Void> loginUser(UserLoginRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);
		
		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
			
			LoginMemberRequest loginRequest = requestMapper.toRequest(request);
			
			bindingResult = BindingResultUtils.validate(loginRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {
				
				MemberDetailsResponse member = memberFeignClient.loginMember(loginRequest).getBody();
				
				if (member.getGroup() != Group.USER) {
					throw UserServiceException.notUserGroupUsername();
				} else {
					return ResponseEntity.status(HttpStatus.NO_CONTENT)
							.header("Access-Token", tokenService.generateAccessToken(member.getId(), member.getGroup()))
							.header("Refresh-Token", tokenService.generateRefreshToken(member.getId()))
							.build();
				}
			}
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

			MemberDetailsResponse member = tokenService.validateRefreshToken(refreshToken, 
					id -> memberFeignClient.getMember(id).getBody());

			if (member.getGroup() != Group.USER) {
				throw UserServiceException.notUserGroupRefreshToken();
			} else {

				tokenService.revokeRefreshToken(refreshToken);

				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.header("Access-Token", tokenService.generateAccessToken(member.getId(), member.getGroup()))
						.header("Refresh-Token", tokenService.generateRefreshToken(member.getId()))
						.build();
			}
		} catch (ExpiredJwtException e) {
			throw UserServiceException.expiredRefreshToken();
		} catch (RevokedJwtException e) {
			throw UserServiceException.revokeRefreshToken();
		} catch (JwtException e) {
			throw UserServiceException.invalidRefreshToken();
		}
	}

	@Override
	public ResponseEntity<Void> modifyUser(UserModifyRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {

			ModifyMemberRequest modifyRequest = requestMapper.toRequest(request);

			bindingResult = BindingResultUtils.validate(modifyRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {

				UUID id = SecurityContextUtils.getAuthenticationId();

				return memberFeignClient.modifyMember(id, modifyRequest);
			}
		}
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseEntity<Void> modifyUserPassword(UserModifyPasswordRequest request) {

		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {

			ModifyMemberPasswordRequest modifyPasswordRequest = requestMapper.toRequest(request);

			bindingResult = BindingResultUtils.validate(modifyPasswordRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {

				UUID id = SecurityContextUtils.getAuthenticationId();
				
				ResponseEntity<Void> response = memberFeignClient.modifyMemberPassword(id, modifyPasswordRequest);
				
				tokenService.revokeTokenAll(id);
				
				return response;
			}
		}
	}

	@Override
	public ResponseEntity<MemberDetailsResponse> getUser() {

		return memberFeignClient.getMember(SecurityContextUtils.getAuthenticationId());
	}

	@Override
	public ResponseEntity<Void> writePost(UserWritePostRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
			
			UUID id = SecurityContextUtils.getAuthenticationId();
			
			CreatePostRequest createRequest = requestMapper.toRequest(request, id);
			
			bindingResult = BindingResultUtils.validate(createRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {
				return postFeignClient.createPost(createRequest);					
			}
		}
	}

	@Override
	public ResponseEntity<Void> modifyPost(UUID postId, UserModifyPostRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {

			 ModifyPostRequest modifyRequest = requestMapper.toRequest(request);
			
			bindingResult = BindingResultUtils.validate(modifyRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {

				UUID id = SecurityContextUtils.getAuthenticationId();

				PostDetailsResponse post = postFeignClient.getPost(postId).getBody();
				
				if (post.getState() == State.DEACTIVE) {
					throw UserServiceException.deactivePost();
				} else if (!post.getWriter().getId().equals(id)) {
					throw UserServiceException.notPostWriter();
				} else {
					return postFeignClient.modifyPost(postId, modifyRequest);
				}
			}
		}
	}

	@Override
	public ResponseEntity<Void> removePost(UUID postId) {
		
		UUID id = SecurityContextUtils.getAuthenticationId();

		PostDetailsResponse.Writer writer = postFeignClient.getPostWriter(postId).getBody();
		
		if (writer.getId().equals(id)) {
			return postFeignClient.modifyPost(postId, ModifyPostRequest.builder().state(State.DEACTIVE).build());
		} else {
			throw UserServiceException.notPostWriter();
		}
	}

	@Override
	public ResponseEntity<PostDetailsResponse> getPost(UUID postId) {

		PostDetailsResponse post = postFeignClient.getPost(postId).getBody();

		if (post.getState() == State.DEACTIVE) {
			throw UserServiceException.deactivePost();
		} else {
			
			postFeignClient.increasePostViews(postId);
			
			return ResponseEntity.status(HttpStatus.OK).body(post);			
		}
	}

	@Override
	public ResponseEntity<PagingListResponse<PostItem>> searchPostList(PagingRequest<UserPostCondition> request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
			
			PagingRequest<PostCondition> pagingRequest = PagingRequest.<PostCondition>builder()
					.condition(requestMapper.toCondition(request.getCondition()))
					.offset(request.getOffset())
					.limit(request.getLimit())
					.build();
			
			bindingResult = BindingResultUtils.validate(pagingRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {
				return searchFeignClient.searchPostList(pagingRequest);
			}
		}
	}

}
