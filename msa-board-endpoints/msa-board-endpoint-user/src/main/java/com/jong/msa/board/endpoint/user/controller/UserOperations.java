package com.jong.msa.board.endpoint.user.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.annotation.APIErrorResponse;
import com.jong.msa.board.endpoint.user.request.UserJoinRequest;
import com.jong.msa.board.endpoint.user.request.UserLoginRequest;
import com.jong.msa.board.endpoint.user.request.UserPasswordModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostSearchRequest;
import com.jong.msa.board.endpoint.user.request.UserPostWriteRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "일반 회원 API")
public interface UserOperations {

	@Operation(summary = "일반 회원 가입")
	@PreAuthorize("isAnonymous()")
	@PostMapping(value = "/apis/users",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.CONFLICT, errorCode = ErrorCode.DUPLICATE_USERNAME)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<Void> joinUser(
			@RequestBody UserJoinRequest request);

	@Operation(summary = "일반 회원 로그인")
	@PreAuthorize("isAnonymous()")
	@PostMapping(value = "/apis/users/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_ADMIN_USERNAME)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_MATCHED_PASSWORD)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> loginUser(
			@RequestBody UserLoginRequest request);
	
	@Operation(summary = "일반 회원 로그아웃")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping(value = "/apis/users/logout")
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
		ResponseEntity<Void> logoutUser(
			@RequestHeader(name = "Access-Token") String accessToken);

	@Operation(summary = "일반 회원 토큰 재발급")
	@PostMapping(value = "/apis/users/refresh",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.EXPIRED_REFRESH_TOKEN)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.REVOKED_REFRESH_TOKEN)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_REFRESH_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> refreshUser(
			@RequestHeader(name = "Refresh-Token") String refreshToken);

	@Operation(summary = "일반 회원 정보 수정")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PatchMapping(value = "/apis/users",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> modifyUser(
			@RequestBody UserModifyRequest request);

	@Operation(summary = "일반 회원 비밀번호 수정")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PatchMapping(value = "/apis/users/password",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_MATCHED_PASSWORD)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> modifyUserPassword(
			@RequestBody UserPasswordModifyRequest request);

	@Operation(summary = "일반 회원 정보 조회")
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/apis/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<MemberDetailsResponse> getUser();
	
	@Operation(summary = "게시물 작성")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping(value = "/apis/users/posts",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.BAD_GATEWAY, errorCode = ErrorCode.UNCHECKED_EXTERNAL_ERROR)
	ResponseEntity<Void> writePost(
			@RequestBody UserPostWriteRequest request);

	@Operation(summary = "게시물 수정")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PatchMapping(value = "/apis/users/posts/{postId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> modifyPost(
			@PathVariable(name = "postId") UUID postId,
			@RequestBody UserPostModifyRequest request);

	@Operation(summary = "게시물 상태 수정")
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping(value = "/apis/users/posts/{postId}")
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<Void> removePost(
			@PathVariable(name = "postId") UUID postId);
	
	@Operation(summary = "게시물 조회")
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/apis/users/posts/{postId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<PostDetailsResponse> getPost(
			@PathVariable(name = "postId") UUID postId);

	@Operation(summary = "게시물 검색")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping(value = "/apis/users/posts",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<PostListResponse> searchPostList(
			UserPostSearchRequest request);
	
}
