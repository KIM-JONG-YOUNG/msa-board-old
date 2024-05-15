package com.jong.msa.board.endpoint.mapper;

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
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.common.enums.DBCodeEnum.Group;
import com.jong.msa.board.endpoint.request.UserJoinMemberRequest;
import com.jong.msa.board.endpoint.request.UserLoginRequest;
import com.jong.msa.board.endpoint.request.UserModifyPasswordRequest;
import com.jong.msa.board.endpoint.request.UserModifyPostRequest;
import com.jong.msa.board.endpoint.request.UserModifyRequest;
import com.jong.msa.board.endpoint.request.UserSearchPostRequest;
import com.jong.msa.board.endpoint.request.UserWritePostRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {

	default Group getUser() { return Group.USER; }
	 
	@Mapping(target = "group", expression = "java(getUser())")
	CreateMemberRequest toRequest(UserJoinMemberRequest request);
	
	LoginMemberRequest toRequest(UserLoginRequest request);
	
	ModifyMemberRequest toRequest(UserModifyRequest request);
	
	ModifyMemberPasswordRequest toRequest(UserModifyPasswordRequest request);
	
	SearchPostRequest toRequest(UserSearchPostRequest request);

	CreatePostRequest toRequest(UserWritePostRequest request, UUID writerId);

	ModifyPostRequest toRequest(UserModifyPostRequest request);

}
