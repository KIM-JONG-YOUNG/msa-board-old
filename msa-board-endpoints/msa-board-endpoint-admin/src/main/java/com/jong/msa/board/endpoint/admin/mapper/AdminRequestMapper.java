package com.jong.msa.board.endpoint.admin.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.search.request.param.MemberCondition;
import com.jong.msa.board.client.search.request.param.PostCondition;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPasswordRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyUserRequest;
import com.jong.msa.board.endpoint.admin.request.AdminWritePostRequest;
import com.jong.msa.board.endpoint.admin.request.param.AdminMemberCondition;
import com.jong.msa.board.endpoint.admin.request.param.AdminPostCondition;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminRequestMapper {

	LoginMemberRequest toRequest(AdminLoginRequest request);
	
	ModifyMemberRequest toRequest(AdminModifyRequest request);
	
	ModifyMemberRequest toRequest(AdminModifyUserRequest request);
 
	ModifyMemberPasswordRequest toRequest(AdminModifyPasswordRequest request);
	
//	SearchMemberRequest toRequest(AdminSearchMemberRequest request);
//	
//	SearchPostRequest toRequest(AdminSearchPostRequest request);

	CreatePostRequest toRequest(AdminWritePostRequest request, UUID writerId);

	ModifyPostRequest toRequest(AdminModifyPostRequest request);

	MemberCondition toCondition(AdminMemberCondition condition);

	PostCondition toCondition(AdminPostCondition condition);

}
