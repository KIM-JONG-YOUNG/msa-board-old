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
import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPasswordRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyUserRequest;
import com.jong.msa.board.endpoint.admin.request.AdminSearchMemberRequest;
import com.jong.msa.board.endpoint.admin.request.AdminSearchPostRequest;
import com.jong.msa.board.endpoint.admin.request.AdminWritePostRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminRequestMapper {

	LoginMemberRequest toRequest(AdminLoginRequest request);
	
	ModifyMemberRequest toRequest(AdminModifyRequest request);
	
	ModifyMemberRequest toRequest(AdminModifyUserRequest request);
 
	ModifyMemberPasswordRequest toRequest(AdminModifyPasswordRequest request);

	SearchMemberRequest.Condition toCondition(AdminSearchMemberRequest.Condition condition);

	SearchMemberRequest toRequest(AdminSearchMemberRequest request);

	CreatePostRequest toRequest(AdminWritePostRequest request, UUID writerId);

	ModifyPostRequest toRequest(AdminModifyPostRequest request);

	SearchPostRequest.Condition toCondition(AdminSearchPostRequest.Condition request);

	SearchPostRequest toRequest(AdminSearchPostRequest request);

}
