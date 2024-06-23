package com.jong.msa.board.endpoint.admin.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.member.feign.MemberFeignClient;
import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.feign.PostFeignClient;
import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.client.search.feign.SearchFeignClient;
import com.jong.msa.board.client.search.request.MemberSearchRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.common.enums.State;
import com.jong.msa.board.core.security.exception.RevokedJwtException;
import com.jong.msa.board.core.security.service.TokenService;
import com.jong.msa.board.core.security.utils.SecurityContextUtils;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.endpoint.admin.mapper.AdminRequestMapper;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminMemberSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPasswordModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostWriteRequest;
import com.jong.msa.board.endpoint.admin.request.AdminUserModifyRequest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminRestController implements AdminOperations {
	
	private final AdminRequestMapper requestMapper;
	
	private final MemberFeignClient memberFeignClient;
	
	private final PostFeignClient postFeignClient;
	
	private final SearchFeignClient searchFeignClient;

	private final TokenService tokenService;

	@Override
	public ResponseEntity<Void> loginAdmin(AdminLoginRequest request) {

		MemberLoginRequest loginRequest = requestMapper.toRequest(request);

		MemberDetailsResponse member = memberFeignClient.loginMember(loginRequest).getBody();

		if (member.getGroup() != Group.ADMIN) {
		
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_ADMIN_USERNAME);
		
		} else {
		
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.header("Access-Token", tokenService.generateAccessToken(member.getId(), member.getGroup()))
					.header("Refresh-Token", tokenService.generateRefreshToken(member.getId()))
					.build();
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

			UUID id = tokenService.validateRefreshToken(refreshToken);
			
			MemberDetailsResponse member = memberFeignClient.getMember(id).getBody();

			if (member.getGroup() == Group.ADMIN) {
				
				tokenService.revokeRefreshToken(refreshToken);

				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.header("Access-Token", tokenService.generateAccessToken(member.getId(), member.getGroup()))
						.header("Refresh-Token", tokenService.generateRefreshToken(member.getId()))
						.build();

			} else {

				throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.REVOKED_REFRESH_TOKEN);
			}
		} catch (ExpiredJwtException e) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.EXPIRED_REFRESH_TOKEN);

		} catch (RevokedJwtException e) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.REVOKED_REFRESH_TOKEN);
			
		} catch (JwtException e) {
			
			throw new RestServiceException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REFRESH_TOKEN);
		}
	}

	@Override
	public ResponseEntity<Void> modifyAdmin(AdminModifyRequest request) {
		
		MemberModifyRequest modifyRequest = requestMapper.toRequest(request);

		UUID id = SecurityContextUtils.getAuthenticationId();

		return memberFeignClient.modifyMember(id, modifyRequest);
	}

	@Override
	public ResponseEntity<Void> modifyAdminPassword(AdminPasswordModifyRequest request) {
		
		MemberPasswordModifyRequest modifyPasswordRequest = requestMapper.toRequest(request);

		UUID id = SecurityContextUtils.getAuthenticationId();

		ResponseEntity<Void> response = memberFeignClient.modifyMemberPassword(id, modifyPasswordRequest);
		
		tokenService.revokeTokenAll(id);
		
		return response;
	}

	@Override
	public ResponseEntity<MemberDetailsResponse> getAdmin() {

		return memberFeignClient.getMember(SecurityContextUtils.getAuthenticationId());
	}

	@Override
	public ResponseEntity<Void> modifyUser(UUID userId, AdminUserModifyRequest request) {

		MemberModifyRequest modifyRequest = requestMapper.toRequest(request);

		MemberDetailsResponse member = memberFeignClient.getMember(userId).getBody();
		
		if (member.getGroup() != Group.USER) {
			
			throw new RestServiceException(HttpStatus.FORBIDDEN, ErrorCode.NOT_USER_GROUP_MEMBER);

		} else {
			
			ResponseEntity<Void> response = memberFeignClient.modifyMember(userId, modifyRequest);
			
			tokenService.revokeTokenAll(userId);
			
			return response;
		}
	}

	@Override
	public ResponseEntity<MemberDetailsResponse> getMember(UUID memberId) {
	
		return memberFeignClient.getMember(memberId);
	} 	

	@Override
	public ResponseEntity<MemberListResponse> searchMemberList(AdminMemberSearchRequest request) {

		MemberSearchRequest searchRequest = requestMapper.toRequest(request);

		return searchFeignClient.searchMemberList(searchRequest);
	}

	@Override
	public ResponseEntity<Void> writePost(AdminPostWriteRequest request) {
		
		UUID id = SecurityContextUtils.getAuthenticationId();

		PostCreateRequest createRequest = requestMapper.toRequest(request, id);

		return postFeignClient.createPost(createRequest);
	}

	@Override
	public ResponseEntity<Void> modifyPost(UUID postId, AdminPostModifyRequest request) {
		
		PostModifyRequest modifyRequest = requestMapper.toRequest(request);

		PostDetailsResponse.Writer writer = postFeignClient.getPostWriter(postId).getBody();
		
		if (writer.getGroup() == Group.ADMIN) {
		
			return postFeignClient.modifyPost(postId, modifyRequest);
			
		} else {
			
			throw new RestServiceException(HttpStatus.FORBIDDEN, ErrorCode.NOT_ADMIN_POST);
		}
	}

	@Override
	public ResponseEntity<Void> modifyPostState(UUID postId, State state) {

		return postFeignClient.modifyPost(postId, PostModifyRequest.builder().state(state).build());
	}
	
	@Override
	public ResponseEntity<PostDetailsResponse> getPost(UUID postId) {

		return postFeignClient.getPost(postId);
	}

	@Override
	public ResponseEntity<PostListResponse> searchPostList(AdminPostSearchRequest request) {
		
		PostSearchRequest searchRequest = requestMapper.toRequest(request);

		return searchFeignClient.searchPostList(searchRequest);
	}

}
