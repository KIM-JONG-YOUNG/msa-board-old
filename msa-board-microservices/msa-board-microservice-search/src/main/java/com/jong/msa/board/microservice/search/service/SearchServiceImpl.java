package com.jong.msa.board.microservice.search.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.enums.SortEnum;
import com.jong.msa.board.common.enums.SortEnum.MemberSort;
import com.jong.msa.board.common.enums.SortEnum.Order;
import com.jong.msa.board.common.enums.SortEnum.PostSort;
import com.jong.msa.board.core.persist.utils.QueryDslUtils;
import com.jong.msa.board.domain.member.entity.QMemberEntity;
import com.jong.msa.board.domain.post.entity.QPostEntity;
import com.jong.msa.board.microservice.search.mapper.SearchEntityMapper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

	private final SearchEntityMapper entityMapper;
	
	private final JPAQueryFactory queryFactory;
	
	private final Map<SortEnum, ComparableExpressionBase<?>> sortMap = new HashMap<>();
	
	@PostConstruct
	private void initSortMap() {

		this.sortMap.put(MemberSort.USERNAME, QMemberEntity.memberEntity.username);
		this.sortMap.put(MemberSort.NAME, QMemberEntity.memberEntity.name);
		this.sortMap.put(MemberSort.EMAIL, QMemberEntity.memberEntity.email);
		this.sortMap.put(MemberSort.CREATED_DATE_TIME, QMemberEntity.memberEntity.createdDateTime);
		this.sortMap.put(MemberSort.UPDATED_DATE_TIME, QMemberEntity.memberEntity.updatedDateTime);

		this.sortMap.put(PostSort.TITLE, QPostEntity.postEntity.title);
		this.sortMap.put(PostSort.CONTENT, QPostEntity.postEntity.content);
		this.sortMap.put(PostSort.WRITER_USERNAME, QMemberEntity.memberEntity.username);
		this.sortMap.put(PostSort.VIEWS, QPostEntity.postEntity.views);
		this.sortMap.put(PostSort.CREATED_DATE_TIME, QPostEntity.postEntity.createdDateTime);
		this.sortMap.put(PostSort.UPDATED_DATE_TIME, QPostEntity.postEntity.updatedDateTime);
	}
	
	@Transactional(readOnly = true)
	@Override
	public MemberListResponse searchMemberList(SearchMemberRequest request) {
		
		BooleanExpression[] searchConditions = new BooleanExpression[] {
				QueryDslUtils.containIfPresent(QMemberEntity.memberEntity.username, request.getUsername()),
				QueryDslUtils.containIfPresent(QMemberEntity.memberEntity.name, request.getName()),
				QueryDslUtils.containIfPresent(QMemberEntity.memberEntity.email, request.getEmail()),
				QueryDslUtils.equalsIfPresent(QMemberEntity.memberEntity.gender, request.getGender()),
				QueryDslUtils.equalsIfPresent(QMemberEntity.memberEntity.group, request.getGroup()),
				QueryDslUtils.equalsIfPresent(QMemberEntity.memberEntity.state, request.getState()),
				QueryDslUtils.afterIfPresent(QMemberEntity.memberEntity.createdDateTime, request.getCreatedDateFrom()),
				QueryDslUtils.beforeIfPresent(QMemberEntity.memberEntity.createdDateTime, request.getCreatedDateTo()),
				QueryDslUtils.afterIfPresent(QMemberEntity.memberEntity.updatedDateTime, request.getUpdatedDateFrom()),
				QueryDslUtils.beforeIfPresent(QMemberEntity.memberEntity.updatedDateTime, request.getUpdatedDateTo()),
		};

		MemberSort sort = (request.getSort() != null) ? request.getSort() : MemberSort.USERNAME;
		Order order = (request.getOrder() != null) ? request.getOrder() : sort.getDefaultOrder();
		
		ComparableExpressionBase<?> orderColumn = sortMap.getOrDefault(sort, QMemberEntity.memberEntity.username);
		OrderSpecifier<?> orderCondition = (order == Order.ASC) ? orderColumn.asc() : orderColumn.desc();
		
		long totalCount = queryFactory
				.select(QMemberEntity.memberEntity.count())
				.from(QMemberEntity.memberEntity)
				.where(searchConditions)
				.fetchOne();
		
		return MemberListResponse.builder()
				.totalCount(totalCount)
				.list(queryFactory
						.selectFrom(QMemberEntity.memberEntity)
						.where(searchConditions)
						.orderBy(orderCondition)
						.offset(request.getOffset())
						.limit(request.getLimit())
						.fetch().stream()
						.map(x -> entityMapper.toListInfo(x))
						.collect(Collectors.toList()))
				.build();
	}

	@Transactional(readOnly = true)
	@Override
	public PostListResponse searchPostList(SearchPostRequest request) {
		
		BooleanExpression[] searchConditions = new BooleanExpression[] {
				QueryDslUtils.containIfPresent(QPostEntity.postEntity.title, request.getTitle()),
				QueryDslUtils.containIfPresent(QPostEntity.postEntity.content, request.getContent()),
				QueryDslUtils.containIfPresent(QMemberEntity.memberEntity.username, request.getWriterUsername()),
				QueryDslUtils.equalsIfPresent(QPostEntity.postEntity.state, request.getState()),
				QueryDslUtils.afterIfPresent(QPostEntity.postEntity.createdDateTime, request.getCreatedDateFrom()),
				QueryDslUtils.beforeIfPresent(QPostEntity.postEntity.createdDateTime, request.getCreatedDateTo()),
				QueryDslUtils.afterIfPresent(QPostEntity.postEntity.updatedDateTime, request.getUpdatedDateFrom()),
				QueryDslUtils.beforeIfPresent(QPostEntity.postEntity.updatedDateTime, request.getUpdatedDateTo()),
		};
 
		PostSort sort = (request.getSort() != null) ? request.getSort() : PostSort.CREATED_DATE_TIME;
		Order order = (request.getOrder() != null) ? request.getOrder() : sort.getDefaultOrder();
		
		ComparableExpressionBase<?> orderColumn = sortMap.getOrDefault(sort, QPostEntity.postEntity.createdDateTime);
		OrderSpecifier<?> orderCondition = (order == Order.ASC) ? orderColumn.asc() : orderColumn.desc();

		long totalCount = queryFactory
				.select(QPostEntity.postEntity.count())
				.from(QPostEntity.postEntity)
				.leftJoin(QMemberEntity.memberEntity)
					.on(QPostEntity.postEntity.writerId.eq(QMemberEntity.memberEntity.id))
				.where(searchConditions)
				.fetchOne();
		
		return PostListResponse.builder()
				.totalCount(totalCount)
				.list(queryFactory
						.select(QPostEntity.postEntity, QMemberEntity.memberEntity)
						.from(QPostEntity.postEntity)
						.leftJoin(QMemberEntity.memberEntity)
							.on(QPostEntity.postEntity.writerId.eq(QMemberEntity.memberEntity.id))
						.where(searchConditions)
						.orderBy(orderCondition)
						.offset(request.getOffset())
						.limit(request.getLimit())
						.fetch().stream()
						.map(x -> entityMapper.toListInfo(
								x.get(QPostEntity.postEntity), 
								x.get(QMemberEntity.memberEntity)))
						.collect(Collectors.toList()))
				.build();
	}

}
