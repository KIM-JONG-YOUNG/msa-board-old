package com.jong.msa.board.client.member.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.core.feign.condition.FeignClientCondition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원 관리 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = "microservice-member")
public interface MemberFeignClient {

	@Operation(summary = "회원 생성")
	@PostMapping(value = "/apis/members",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> createMember(
			@RequestBody CreateMemberRequest request);

	@Operation(summary = "회원 수정")
	@PatchMapping(value = "/apis/members/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyMember(
			@PathVariable(name = "id") UUID id,
			@RequestBody ModifyMemberRequest request);

	@Operation(summary = "회원 비밀번호 수정")
	@PatchMapping(value = "/apis/members/{id}/password",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> modifyMemberPassword(
			@PathVariable(name = "id") UUID id,
			@RequestBody ModifyMemberPasswordRequest request);
	
	@Operation(summary = "회원 조회")
	@GetMapping(value = "/apis/members/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberDetailsResponse> getMember(
			@PathVariable(name = "id") UUID id);
	
	@Operation(summary = "회원 로그인")
	@PostMapping(value = "/apis/members/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<MemberDetailsResponse> loginMember(
			@RequestBody LoginMemberRequest request);

}
