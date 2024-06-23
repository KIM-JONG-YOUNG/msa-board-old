package com.jong.msa.board.endpoint.admin.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.client.search.request.MemberSearchRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPasswordModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminUserModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminMemberSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostWriteRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminRequestMapper {
 
	MemberLoginRequest toRequest(AdminLoginRequest request);
	
	MemberModifyRequest toRequest(AdminModifyRequest request);
	
	MemberModifyRequest toRequest(AdminUserModifyRequest request);

	MemberPasswordModifyRequest toRequest(AdminPasswordModifyRequest request);

	MemberSearchRequest.Condition toCondition(AdminMemberSearchRequest.Condition condition);

	MemberSearchRequest toRequest(AdminMemberSearchRequest request);

	PostCreateRequest toRequest(AdminPostWriteRequest request, UUID writerId);

	PostModifyRequest toRequest(AdminPostModifyRequest request);

	PostSearchRequest.Condition toCondition(AdminPostSearchRequest.Condition condition);

	PostSearchRequest toRequest(AdminPostSearchRequest request);

}
