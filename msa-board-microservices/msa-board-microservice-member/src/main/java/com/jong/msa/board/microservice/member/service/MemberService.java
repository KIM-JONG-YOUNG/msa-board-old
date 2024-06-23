package com.jong.msa.board.microservice.member.service;

import java.util.UUID;

import com.jong.msa.board.client.member.request.MemberCreateRequest;
import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;

public interface MemberService {

	UUID createMember(MemberCreateRequest request);

	void modifyMember(UUID id, MemberModifyRequest request);

	void modifyMemberPassword(UUID id, MemberPasswordModifyRequest request);

	MemberDetailsResponse getMember(UUID id);

	MemberDetailsResponse loginMember(MemberLoginRequest request);

}
