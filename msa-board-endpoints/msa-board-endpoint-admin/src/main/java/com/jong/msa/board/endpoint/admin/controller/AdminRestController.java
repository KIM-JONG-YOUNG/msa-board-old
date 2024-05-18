package com.jong.msa.board.endpoint.admin.controller;

import java.time.format.DateTimeFormatter;
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
import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.constants.DateTimeFormats;
import com.jong.msa.board.common.enums.DBCodeEnum.Group;
import com.jong.msa.board.common.enums.DBCodeEnum.State;
import com.jong.msa.board.common.enums.ErrorCodeEnum.TokenErrorCode;
import com.jong.msa.board.core.security.service.TokenService;
import com.jong.msa.board.core.security.service.TokenService.TokenGenerateResult;
import com.jong.msa.board.core.security.service.TokenService.TokenValidResult;
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
import com.jong.msa.board.endpoint.admin.request.AdminSearchMemberRequest;
import com.jong.msa.board.endpoint.admin.request.AdminSearchPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminWritePostRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminRestController implements AdminOperations {

	private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DateTimeFormats.DATE_TIME_FORMAT);

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
					
					TokenGenerateResult accessTokenResult = tokenService.generateAccessToken(member.getId(), member.getGroup()); 
					TokenGenerateResult refreshTokenResult = tokenService.generateRefreshToken(member.getId());

					return ResponseEntity.status(HttpStatus.NO_CONTENT)
							.header("Access-Token", accessTokenResult.getToken())
							.header("Refresh-Token", refreshTokenResult.getToken())
							.header("Access-Token-Expired-Time", DATE_TIME_FORMATTER.format(accessTokenResult.getExpiredTime()))
							.header("Refresh-Token-Expired-Time", DATE_TIME_FORMATTER.format(refreshTokenResult.getExpiredTime()))
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

		TokenValidResult validResult = tokenService.validateRefreshToken(refreshToken);
		
		if (validResult.isValid()) {

			MemberDetailsResponse member = memberFeignClient.getMember(validResult.getId()).getBody();

			TokenGenerateResult accessTokenResult = tokenService.generateAccessToken(member.getId(), member.getGroup()); 
			TokenGenerateResult refreshTokenResult = tokenService.generateRefreshToken(member.getId());

			tokenService.revokeRefreshToken(refreshToken);

			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.header("Access-Token", accessTokenResult.getToken())
					.header("Refresh-Token", refreshTokenResult.getToken())
					.header("Access-Token-Expired-Time", DATE_TIME_FORMATTER.format(accessTokenResult.getExpiredTime()))
					.header("Refresh-Token-Expired-Time", DATE_TIME_FORMATTER.format(refreshTokenResult.getExpiredTime()))
					.build();
		} else {

			TokenErrorCode tokenErrorCode = validResult.getErrorCode();

			switch (tokenErrorCode) {
			case EXPIRED_REFRESH_TOKEN:
				throw AdminServiceException.expiredRefreshToken();
			case REVOKED_REFRESH_TOKEN:
				throw AdminServiceException.revokeRefreshToken();
			case INVALID_REFRESH_TOKEN:
				throw AdminServiceException.invalidRefreshToken();
			default:
				throw RestServiceException.uncheckedError(HttpStatus.INTERNAL_SERVER_ERROR);
			}
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
	public ResponseEntity<MemberListResponse> searchMemberList(AdminSearchMemberRequest request) {

		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
		
			SearchMemberRequest searchRequest = requestMapper.toRequest(request);
			
			bindingResult = BindingResultUtils.validate(searchRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {
				return searchFeignClient.searchMemberList(searchRequest);
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
	public ResponseEntity<PostListResponse> searchPostList(AdminSearchPostRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult);
		} else {
			
			SearchPostRequest searchRequest = requestMapper.toRequest(request);
			
			bindingResult = BindingResultUtils.validate(searchRequest, validator);
			
			if (bindingResult.hasErrors()) {
				throw RestServiceException.invalidParameter(bindingResult);
			} else {
				return searchFeignClient.searchPostList(searchRequest);
			}
		}
	}

}
