package com.jong.msa.board.endpoint.user.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.search.request.param.PostCondition;
import com.jong.msa.board.common.enums.CodeEnum.Group;
import com.jong.msa.board.common.enums.CodeEnum.State;
import com.jong.msa.board.endpoint.user.request.UserJoinMemberRequest;
import com.jong.msa.board.endpoint.user.request.UserLoginRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyPasswordRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyPostRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserWritePostRequest;
import com.jong.msa.board.endpoint.user.request.param.UserPostCondition;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {

	default Group getUser() { return Group.USER; }

	default State getActive() { return State.ACTIVE; }

	@Mapping(target = "group", expression = "java(getUser())")
	CreateMemberRequest toRequest(UserJoinMemberRequest request);
	
	LoginMemberRequest toRequest(UserLoginRequest request);
	
	ModifyMemberRequest toRequest(UserModifyRequest request);
	
	ModifyMemberPasswordRequest toRequest(UserModifyPasswordRequest request);
	
	@Mapping(target = "state", expression = "java(getActive())")
	PostCondition toCondition(UserPostCondition condition);

	CreatePostRequest toRequest(UserWritePostRequest request, UUID writerId);

	ModifyPostRequest toRequest(UserModifyPostRequest request);

}
