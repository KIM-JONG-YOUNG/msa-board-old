package com.jong.msa.board.client.member.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jong.msa.board.client.core.condition.FeignClientCondition;
import com.jong.msa.board.client.member.request.MemberCreateRequest;
import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.common.constants.MicroserviceNames;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.annotation.APIErrorResponse;
import com.jong.msa.board.core.web.annotation.APIErrorResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원 관리 API")
@Conditional(FeignClientCondition.class)
@FeignClient(name = MicroserviceNames.MEMBER_MICROSERVICE)
@APIErrorResponses({
	@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.INVALID_PARAMETER, useErrorDetailsList = true),
	@APIErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR, errorCode = ErrorCode.UNCHECKED_INTERNAL_ERROR)
})
public interface MemberFeignClient {

	@Operation(summary = "회원 생성")
	@PostMapping(value = "/apis/members",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.CONFLICT, errorCode = ErrorCode.DUPLICATE_USERNAME)
	})
	ResponseEntity<Void> createMember(
			@RequestBody MemberCreateRequest request);

	@Operation(summary = "회원 수정")
	@PatchMapping(value = "/apis/members/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	})
	ResponseEntity<Void> modifyMember(
			@PathVariable(name = "id") UUID id,
			@RequestBody MemberModifyRequest request);

	@Operation(summary = "회원 비밀번호 수정")
	@PatchMapping(value = "/apis/members/{id}/password",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_MATCHED_PASSWORD),
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	})
	ResponseEntity<Void> modifyMemberPassword(
			@PathVariable(name = "id") UUID id,
			@RequestBody MemberPasswordModifyRequest request);
	
	@Operation(summary = "회원 조회")
	@GetMapping(value = "/apis/members/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.GONE, errorCode = ErrorCode.NOT_FOUND_MEMBER)
	})
	ResponseEntity<MemberDetailsResponse> getMember(
			@PathVariable(name = "id") UUID id);
	
	@Operation(summary = "회원 로그인")
	@PostMapping(value = "/apis/members/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@APIErrorResponses({
		@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_FOUND_MEMBER),
		@APIErrorResponse(status = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.NOT_MATCHED_PASSWORD)
	})
	ResponseEntity<MemberDetailsResponse> loginMember(
			@RequestBody MemberLoginRequest request);

}
