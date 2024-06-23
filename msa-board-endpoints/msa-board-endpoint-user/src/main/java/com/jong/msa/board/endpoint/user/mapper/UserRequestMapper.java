package com.jong.msa.board.endpoint.user.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.jong.msa.board.client.member.request.MemberCreateRequest;
import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.common.enums.State;
import com.jong.msa.board.endpoint.user.request.UserJoinRequest;
import com.jong.msa.board.endpoint.user.request.UserLoginRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPasswordModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostSearchRequest;
import com.jong.msa.board.endpoint.user.request.UserPostWriteRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {

	default Group getUser() { return Group.USER; }

	default State getActive() { return State.ACTIVE; }

	@Mapping(target = "group", expression = "java(getUser())")
	MemberCreateRequest toRequest(UserJoinRequest request);
	
	MemberLoginRequest toRequest(UserLoginRequest request);
	
	MemberModifyRequest toRequest(UserModifyRequest request);
	
	MemberPasswordModifyRequest toRequest(UserPasswordModifyRequest request);

	PostCreateRequest toRequest(UserPostWriteRequest request, UUID writerId);

	PostModifyRequest toRequest(UserPostModifyRequest request);

	@Mapping(target = "state", expression = "java(getActive())")
	PostSearchRequest.Condition toCondition(UserPostSearchRequest.Condition condition);

	PostSearchRequest toRequest(UserPostSearchRequest request);

}
