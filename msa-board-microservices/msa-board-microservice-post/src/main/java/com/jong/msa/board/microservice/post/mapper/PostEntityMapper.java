package com.jong.msa.board.microservice.post.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.domain.post.entity.PostEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostEntityMapper {

	PostEntity toEntity(PostCreateRequest request);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	PostEntity updateEntity(PostModifyRequest request, @MappingTarget PostEntity entity);

	PostDetailsResponse.Writer toWriter(MemberDetailsResponse response);

	@Mapping(target = "id", source = "entity.id")
	@Mapping(target = "writer", source = "writer")
	@Mapping(target = "createdDateTime", source = "entity.createdDateTime")
	@Mapping(target = "updatedDateTime", source = "entity.updatedDateTime")
	@Mapping(target = "state", source = "entity.state")
	PostDetailsResponse toResponse(PostEntity entity, PostDetailsResponse.Writer writer);
	
}
