package com.jong.msa.board.endpoint.admin.controller;

import java.util.UUID;

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
import com.jong.msa.board.common.enums.CodeEnum.State;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPasswordRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyUserRequest;
import com.jong.msa.board.endpoint.admin.request.AdminSearchMemberRequest;
import com.jong.msa.board.endpoint.admin.request.AdminSearchPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminWritePostRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "관리자 회원 API")
public interface AdminOperations {

	@Operation(summary = "관리자 회원 로그인")
	@PreAuthorize("isAnonymous()")
	@PostMapping(value = "/apis/admins/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> loginAdmin(
			@RequestBody AdminLoginRequest request);
	
	@Operation(summary = "관리자 회원 로그아웃")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/apis/admins/logout")
	ResponseEntity<Void> logoutAdmin(
			@RequestHeader(name = "Access-Token") String accessToken);

	@Operation(summary = "관리자 회원 토큰 재발급")
	@PostMapping(value = "/apis/admins/refresh",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> refreshAdmin(
			@RequestHeader(name = "Refresh-Token") String refreshToken);

	@Operation(summary = "관리자 회원 정보 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyAdmin(
			@RequestBody AdminModifyRequest request);

	@Operation(summary = "관리자 회원 비밀번호 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/password",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyAdminPassword(
			@RequestBody AdminModifyPasswordRequest request);

	@Operation(summary = "관리자 회원 정보 조회")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/apis/admins",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberDetailsResponse> getAdmin();

	@Operation(summary = "일반 회원 정보 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/users/{userId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyUser(
			@PathVariable(name = "userId") UUID userId,
			@RequestBody AdminModifyUserRequest request);

	@Operation(summary = "회원 정보 조회")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/apis/admins/members/{memberId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberDetailsResponse> getMember(
			@PathVariable(name = "memberId") UUID memberId);

	@Operation(summary = "회원 검색")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/apis/admins/members/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberListResponse> searchMemberList(
			@RequestBody AdminSearchMemberRequest request);

	@Operation(summary = "게시물 작성")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/apis/admins/posts",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> writePost(
			@RequestBody AdminWritePostRequest request);

	@Operation(summary = "관리자 게시물 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/posts/{postId}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyPost(
			@PathVariable(name = "postId") UUID postId,
			@RequestBody AdminModifyPostRequest request);

	@Operation(summary = "게시물 상태 수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping(value = "/apis/admins/posts/{postId}/state",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyPostState(
			@PathVariable(name = "postId") UUID postId,
			@RequestBody State state);
	
	@Operation(summary = "게시물 조회")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/apis/admins/posts/{postId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PostDetailsResponse> getPost(
			@PathVariable(name = "postId") UUID postId);

	@Operation(summary = "게시물 검색")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/apis/admins/posts/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PostListResponse> searchPostList(
			@RequestBody AdminSearchPostRequest request);

}
