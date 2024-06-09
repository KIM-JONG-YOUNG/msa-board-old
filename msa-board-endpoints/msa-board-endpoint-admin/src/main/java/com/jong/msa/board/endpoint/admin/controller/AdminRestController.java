package com.jong.msa.board.endpoint.admin.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.member.feign.MemberFeignClient;
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
import com.jong.msa.board.client.search.request.param.MemberCondition;
import com.jong.msa.board.client.search.request.param.PostCondition;
import com.jong.msa.board.client.search.response.PagingListResponse;
import com.jong.msa.board.client.search.response.result.MemberItem;
import com.jong.msa.board.client.search.response.result.PostItem;
import com.jong.msa.board.common.enums.CodeEnum.Group;
import com.jong.msa.board.common.enums.CodeEnum.State;
import com.jong.msa.board.core.security.exception.RevokedJwtException;
import com.jong.msa.board.core.security.service.TokenService;
import com.jong.msa.board.core.security.utils.SecurityContextUtils;
import com.jong.msa.board.core.validation.utils.BindingResultUtils;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.endpoint.admin.exception.AdminServiceException;
import com.jong.msa.board.endpoint.admin.mapper.AdminRequestMapper;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPasswordRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyUserRequest;
import com.jong.msa.board.endpoint.admin.request.AdminWritePostRequest;
import com.jong.msa.board.endpoint.admin.request.param.AdminMemberCondition;
import com.jong.msa.board.endpoint.admin.request.param.AdminPostCondition;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminRestController implements AdminOperations {

	private final Validator validator;
	
	private final AdminRequestMapper requestMapper;
	
	private final MemberFeignClient memberFeignClient;
	
	private final PostFeignClient postFeignClient;
	
	private final SearchFeignClient searchFeignClient;

	private final TokenService tokenService;

	@Override
	public ResponseEntity<Void> loginAdmin(AdminLoginRequest request) {

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
				
				if (member.getGroup() != Group.ADMIN) {
					throw AdminServiceException.notAdminGroupUsername();
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
	public ResponseEntity<Void> logoutAdmin(String accessToken) {
		
		tokenService.revokeAccessToken(accessToken);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	public ResponseEntity<Void> refreshAdmin(String refreshToken) {

		try {

			MemberDetailsResponse member = tokenService.validateRefreshToken(refreshToken, memberFeignClient::getMember).getBody();

			if (member.getGroup() != Group.ADMIN) {
				throw AdminServiceException.notAdminGroupRefreshToken();
			} else {

				tokenService.revokeRefreshToken(refreshToken);

				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.header("Access-Token", tokenService.generateAccessToken(member.getId(), member.getGroup()))
						.header("Refresh-Token", tokenService.generateRefreshToken(member.getId()))
						.build();
			}
		} catch (ExpiredJwtException e) {
			throw AdminServiceException.expiredRefreshToken();
		} catch (RevokedJwtException e) {
			throw AdminServiceException.revokeRefreshToken();
		} catch (JwtException e) {
			throw AdminServiceException.invalidRefreshToken();
		}
	}

	@Override
	public ResponseEntity<Void> modifyAdmin(AdminModifyRequest request) {
		
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

	@Override
	public ResponseEntity<Void> modifyAdminPassword(AdminModifyPasswordRequest request) {
		
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
	public ResponseEntity<MemberDetailsResponse> getAdmin() {

		return memberFeignClient.getMember(SecurityContextUtils.getAuthenticationId());
	}

	@Override
	public ResponseEntity<Void> modifyUser(UUID userId, AdminModifyUserRequest request) {

		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
		
			ModifyMemberRequest modifyRequest = requestMapper.toRequest(request);

			bindingResult = BindingResultUtils.validate(modifyRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {

				MemberDetailsResponse member = memberFeignClient.getMember(userId).getBody();
				
				if (member.getGroup() != Group.USER) {
					throw AdminServiceException.notUserGroupMember();
				} else {
					
					ResponseEntity<Void> response = memberFeignClient.modifyMember(userId, modifyRequest);
					
					tokenService.revokeTokenAll(userId);
					
					return response;
				}
			}
		}
	}

	@Override
	public ResponseEntity<MemberDetailsResponse> getMember(UUID memberId) {
	
		return memberFeignClient.getMember(memberId);
	} 	

	@Override
	public ResponseEntity<PagingListResponse<MemberItem>> searchMemberList(PagingRequest<AdminMemberCondition> request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
		
			PagingRequest<MemberCondition> pagingRequest = PagingRequest.<MemberCondition>builder()
					.condition(requestMapper.toCondition(request.getCondition()))
					.offset(request.getOffset())
					.limit(request.getLimit())
					.build(); 
			
			bindingResult = BindingResultUtils.validate(pagingRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {
				return searchFeignClient.searchMemberList(pagingRequest);
			}
		}
	}


	@Override
	public ResponseEntity<Void> writePost(AdminWritePostRequest request) {
		
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
	public ResponseEntity<Void> modifyPost(UUID postId, AdminModifyPostRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {

			 ModifyPostRequest modifyRequest = requestMapper.toRequest(request);
			
			bindingResult = BindingResultUtils.validate(modifyRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {

				PostDetailsResponse.Writer writer = postFeignClient.getPostWriter(postId).getBody();
				
				if (writer.getGroup() == Group.ADMIN) {
					return postFeignClient.modifyPost(postId, modifyRequest);
				} else {
					throw AdminServiceException.notAdminGroupPost();
				}
			}
		}
	}

	@Override
	public ResponseEntity<Void> modifyPostState(UUID postId, State state) {

		return postFeignClient.modifyPost(postId, ModifyPostRequest.builder().state(state).build());
	}
	
	@Override
	public ResponseEntity<PostDetailsResponse> getPost(UUID postId) {

		return postFeignClient.getPost(postId);
	}

	@Override
	public ResponseEntity<PagingListResponse<PostItem>> searchPostList(PagingRequest<AdminPostCondition> request) {
		
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
