package com.jong.msa.board.microservice.search.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.common.enums.MemberSort;
import com.jong.msa.board.common.enums.Order;
import com.jong.msa.board.common.enums.PostSort;
import com.jong.msa.board.common.enums.SortEnum;
import com.jong.msa.board.domain.core.utils.QueryDslUtils;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.entity.QMemberEntity;
import com.jong.msa.board.domain.post.entity.QPostEntity;
import com.jong.msa.board.microservice.search.mapper.SearchEntityMapper;
import com.querydsl.core.Tuple;
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
	
	private final QMemberEntity memberEntity = QMemberEntity.memberEntity;

	private final QPostEntity postEntity = QPostEntity.postEntity;

	@PostConstruct
	private void initSortMap() {

		this.sortMap.put(MemberSort.USERNAME, QMemberEntity.memberEntity.username);
		this.sortMap.put(MemberSort.NAME, QMemberEntity.memberEntity.name);
		this.sortMap.put(MemberSort.EMAIL, QMemberEntity.memberEntity.email);
		this.sortMap.put(MemberSort.CREATED_DATE_TIME, QMemberEntity.memberEntity.createdDateTime);
		this.sortMap.put(MemberSort.UPDATED_DATE_TIME, QMemberEntity.memberEntity.updatedDateTime);

		this.sortMap.put(PostSort.TITLE, QPostEntity.postEntity.title);
		this.sortMap.put(PostSort.CONTENT, QPostEntity.postEntity.content);
		this.sortMap.put(PostSort.WRITER, QMemberEntity.memberEntity.username);
		this.sortMap.put(PostSort.VIEWS, QPostEntity.postEntity.views);
		this.sortMap.put(PostSort.CREATED_DATE_TIME, QPostEntity.postEntity.createdDateTime);
		this.sortMap.put(PostSort.UPDATED_DATE_TIME, QPostEntity.postEntity.updatedDateTime);
	}

	@Transactional(readOnly = true)
	@Override
	public MemberListResponse searchMemberList(SearchMemberRequest request) {
		
		BooleanExpression[] searchCondition = new BooleanExpression[] {

				QueryDslUtils.equalsIfPresent(memberEntity.gender, request.getGender()),
				QueryDslUtils.equalsIfPresent(memberEntity.group, request.getGroup()),
				QueryDslUtils.equalsIfPresent(memberEntity.state, request.getState()),

				QueryDslUtils.containIfPresent(memberEntity.username, request.getUsername()),
				QueryDslUtils.containIfPresent(memberEntity.name, request.getName()),
				QueryDslUtils.containIfPresent(memberEntity.email, request.getEmail()),

				QueryDslUtils.afterIfPresent(memberEntity.createdDateTime, request.getCreatedDateFrom()),
				QueryDslUtils.beforeIfPresent(memberEntity.createdDateTime, request.getCreatedDateTo()),

				QueryDslUtils.afterIfPresent(memberEntity.updatedDateTime, request.getUpdatedDateFrom()),
				QueryDslUtils.beforeIfPresent(memberEntity.updatedDateTime, request.getUpdatedDateTo()),
		};

		MemberSort sort = (request.getSort() != null) ? request.getSort() : MemberSort.USERNAME;
		Order order = (request.getOrder() != null) ? request.getOrder() : sort.getDefaultOrder();
		
		ComparableExpressionBase<?> column = sortMap.get(sort);

		OrderSpecifier<?> orderCondition = (order == Order.ASC) ? column.asc() : column.desc();
		
		long totalCount = queryFactory
				.select(memberEntity.count())
				.from(memberEntity)
				.where(searchCondition)
				.fetchOne();

		List<MemberEntity> list = queryFactory
				.selectFrom(memberEntity)
				.where(searchCondition)
				.orderBy(orderCondition)
				.offset(request.getOffset())
				.limit(request.getLimit())
				.fetch(); 
		
		return MemberListResponse.builder()
				.totalCount(totalCount)
				.list(list.stream()
						.map(x -> entityMapper.toListItem(x))
						.collect(Collectors.toList()))
				.build();
	}

	@Transactional(readOnly = true)
	@Override
	public PostListResponse searchPostList(SearchPostRequest request) {
		
		BooleanExpression[] searchCondition = new BooleanExpression[] {

				QueryDslUtils.equalsIfPresent(postEntity.state, request.getState()),

				QueryDslUtils.containIfPresent(postEntity.title, request.getTitle()),
				QueryDslUtils.containIfPresent(memberEntity.username, request.getWriterUsername()),
				QueryDslUtils.containIfPresent(postEntity.content, request.getContent()),

				QueryDslUtils.afterIfPresent(memberEntity.createdDateTime, request.getCreatedDateFrom()),
				QueryDslUtils.beforeIfPresent(memberEntity.createdDateTime, request.getCreatedDateTo()),

				QueryDslUtils.afterIfPresent(memberEntity.updatedDateTime, request.getUpdatedDateFrom()),
				QueryDslUtils.beforeIfPresent(memberEntity.updatedDateTime, request.getUpdatedDateTo()),
		};

		PostSort sort = (request.getSort() != null) ? request.getSort() : PostSort.CREATED_DATE_TIME;
		Order order = (request.getOrder() != null) ? request.getOrder() : sort.getDefaultOrder();
		
		ComparableExpressionBase<?> column = sortMap.get(sort);

		OrderSpecifier<?> orderCondition = (order == Order.ASC) ? column.asc() : column.desc();
		
		long totalCount = queryFactory
				.select(postEntity.count())
				.from(postEntity)
				.leftJoin(memberEntity)
					.on(postEntity.writerId.eq(memberEntity.id))
				.where(searchCondition)
				.fetchOne();		

		List<Tuple> list = queryFactory
				.select(postEntity, memberEntity)
				.from(postEntity)
				.leftJoin(memberEntity)
					.on(postEntity.writerId.eq(memberEntity.id))
				.where(searchCondition)
				.orderBy(orderCondition)
				.offset(request.getOffset())
				.limit(request.getLimit())
				.fetch();		

		return PostListResponse.builder()
				.totalCount(totalCount)
				.list(list.stream()
						.map(x -> entityMapper.toListItem(
								x.get(postEntity),
								x.get(memberEntity)))
						.collect(Collectors.toList()))
				.build();
	}
	
}
