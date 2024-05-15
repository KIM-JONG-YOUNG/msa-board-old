package com.jong.msa.board.microservice.member.controller;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.client.member.feign.MemberFeignClient;
import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.core.validation.utils.BindingResultUtils;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.microservice.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberRestController implements MemberFeignClient {

	private final Validator validator;

	private final MemberService service;

	@Override
	public ResponseEntity<Void> createMember(CreateMemberRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);

		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult); 
		} else {
			return ResponseEntity.status(HttpStatus.CREATED)
					.header(HttpHeaders.LOCATION, new StringBuilder("/apis/members/")
							.append(service.createMember(request))
							.toString())
					.build();
		}
	}

	@Override
	public ResponseEntity<Void> modifyMember(UUID id, ModifyMemberRequest request) {
		
		BindingResult bindingResult = BindingResultUtils.validate(request, validator);
		
		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult); 
		} else {
			
			service.modifyMember(id, request);
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.header(HttpHeaders.LOCATION, new StringBuilder()
							.append("/apis/members/").append(id)
							.toString())
					.build();
		}
	}

	@Override
	public ResponseEntity<Void> modifyMemberPassword(UUID id, ModifyMemberPasswordRequest request) {
		// TODO Auto-generated method stub
		
		service.modifyMemberPassword(id, request);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.header(HttpHeaders.LOCATION, new StringBuilder()
						.append("/apis/members/").append(id)
						.toString())
				.build();
	}

	@Override
	public ResponseEntity<MemberDetailsResponse> getMember(UUID id) {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.getMember(id));
	}

	@Override
	public ResponseEntity<MemberDetailsResponse> loginMember(LoginMemberRequest request) {

		BindingResult bindingResult = BindingResultUtils.validate(request, validator);
		
		if (bindingResult.hasErrors()) {
			throw RestServiceException.invalidParameter(bindingResult); 
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(service.loginMember(request));
		}
	}

}
