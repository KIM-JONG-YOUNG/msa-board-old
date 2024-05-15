package com.jong.msa.board.microservice.member.service;

import java.util.UUID;

import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;

public interface MemberService {

	UUID createMember(CreateMemberRequest request);

	void modifyMember(UUID id, ModifyMemberRequest request);

	void modifyMemberPassword(UUID id, ModifyMemberPasswordRequest request);

	MemberDetailsResponse getMember(UUID id);

	MemberDetailsResponse loginMember(LoginMemberRequest request);

}
