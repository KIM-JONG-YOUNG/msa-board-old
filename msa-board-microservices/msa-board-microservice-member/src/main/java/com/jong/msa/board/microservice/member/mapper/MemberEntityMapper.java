package com.jong.msa.board.microservice.member.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.domain.member.entity.MemberEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberEntityMapper {

	MemberEntity toEntity(CreateMemberRequest request);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	MemberEntity updateEntity(ModifyMemberRequest request, @MappingTarget MemberEntity entity);

	MemberDetailsResponse toResponse(MemberEntity entity);

}
