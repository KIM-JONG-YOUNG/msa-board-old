package com.jong.msa.board.endpoint.admin.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.common.enums.State;
import com.jong.msa.board.core.web.annotation.APIErrorResponse;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPasswordModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminUserModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminMemberSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostWriteRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "관리자 회원 API")
public interface AdminOperations {

	@Operation(summary = "관리자 회원 로그인")
	@PreAuthorize("isAnonymous()")
	@PostMapping(value = "/apis/admins/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_ADMIN_USERNAME)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_MATCHED_PASSWORD)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> loginAdmin(
			@RequestBody AdminLoginRequest request);
	
	@Operation(summary = "관리자 회원 로그아웃")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/apis/admins/logout")
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<Void> logoutAdmin(
			@RequestHeader(name = "Access-Token") String accessToken);

	@Operation(summary = "관리자 회원 토큰 재발급")
	@PostMapping(value = "/apis/admins/refresh",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.EXPIRED_REFRESH_TOKEN)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.REVOKED_REFRESH_TOKEN)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_REFRESH_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> refreshAdmin(
			@RequestHeader(name = "Refresh-Token") String refreshToken);

	@Operation(summary = "관리자 회원 정보 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> modifyAdmin(
			@RequestBody AdminModifyRequest request);

	@Operation(summary = "관리자 회원 비밀번호 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/password",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_MATCHED_PASSWORD)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> modifyAdminPassword(
			@RequestBody AdminPasswordModifyRequest request);

	@Operation(summary = "관리자 회원 정보 조회")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/apis/admins",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<MemberDetailsResponse> getAdmin();

	@Operation(summary = "일반 회원 정보 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/users/{userId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<Void> modifyUser(
			@PathVariable(name = "userId") UUID userId,
			@RequestBody AdminUserModifyRequest request);

	@Operation(summary = "회원 정보 조회")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/apis/admins/members/{memberId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	ResponseEntity<MemberDetailsResponse> getMember(
			@PathVariable(name = "memberId") UUID memberId);

	@Operation(summary = "회원 검색")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/apis/admins/members/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<MemberListResponse> searchMemberList(
			@RequestBody AdminMemberSearchRequest request);

	@Operation(summary = "게시물 작성")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/apis/admins/posts",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<Void> writePost(
			@RequestBody AdminPostWriteRequest request);

	@Operation(summary = "관리자 게시물 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/posts/{postId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ADMIN_POST)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	ResponseEntity<Void> modifyPost(
			@PathVariable(name = "postId") UUID postId,
			@RequestBody AdminPostModifyRequest request);

	@Operation(summary = "게시물 상태 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/posts/{postId}/state",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	ResponseEntity<Void> modifyPostState(
			@PathVariable(name = "postId") UUID postId,
			@RequestBody State state);
	
	@Operation(summary = "게시물 조회")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/apis/admins/posts/{postId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_POST_WRITER)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_POST)
	ResponseEntity<PostDetailsResponse> getPost(
			@PathVariable(name = "postId") UUID postId);

	@Operation(summary = "게시물 검색")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/apis/admins/posts/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.REVOKED_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.INVALID_ACCESS_TOKEN)
	@APIErrorResponse(status = HttpStatus.FORBIDDEN, errorCode = ErrorCode.NOT_ACCESSIBLE_URL)
	ResponseEntity<PostListResponse> searchPostList(
			@RequestBody AdminPostSearchRequest request);

}
