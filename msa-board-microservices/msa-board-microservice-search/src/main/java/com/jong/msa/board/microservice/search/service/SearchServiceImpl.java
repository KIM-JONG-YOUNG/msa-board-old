package com.jong.msa.board.microservice.search.service;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.search.request.DateRange;
import com.jong.msa.board.client.search.request.MemberSearchRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
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
	public MemberListResponse searchMemberList(MemberSearchRequest request) {
		
		MemberSearchRequest.Condition condition = request.getCondition();
		
		DateRange createdDate = (condition == null) ? null : condition.getCreatedDate();
		DateRange updatedDate = (condition == null) ? null : condition.getUpdatedDate();
		
		BooleanExpression[] searchConditions = (condition == null) ? null : new BooleanExpression[] {

				QueryDslUtils.equalsIfPresent(memberEntity.gender, condition.getGender()),
				QueryDslUtils.equalsIfPresent(memberEntity.group, condition.getGroup()),
				QueryDslUtils.equalsIfPresent(memberEntity.state, condition.getState()),

				QueryDslUtils.containIfPresent(memberEntity.username, condition.getUsername()),
				QueryDslUtils.containIfPresent(memberEntity.name, condition.getName()),
				QueryDslUtils.containIfPresent(memberEntity.email, condition.getEmail()),

				(createdDate == null) ? null 
						: QueryDslUtils.afterIfPresent(memberEntity.createdDateTime, createdDate.getFrom()),
				(createdDate == null) ? null 
						: QueryDslUtils.beforeIfPresent(memberEntity.createdDateTime, createdDate.getTo()),

				(updatedDate == null) ? null 
						: QueryDslUtils.afterIfPresent(memberEntity.createdDateTime, updatedDate.getFrom()),
				(updatedDate == null) ? null 
						: QueryDslUtils.beforeIfPresent(memberEntity.createdDateTime, updatedDate.getTo()),
		};

		OrderSpecifier<?>[] orderConditions = (request.getSortOrderList() == null) 
				? new OrderSpecifier<?>[] { memberEntity.username.asc() }
				: request.getSortOrderList().stream().map(x -> {
					
					MemberSort sort = (x.getSort() != null) ? x.getSort() : MemberSort.USERNAME;
					Order order = (x.getOrder() != null) ? x.getOrder() : sort.getDefaultOrder();
					
					ComparableExpressionBase<?> column = sortMap.getOrDefault(sort, memberEntity.username);
					
					return (order == Order.ASC) ? column.asc() : column.desc();
				})
				.collect(Collectors.toCollection(LinkedHashSet::new)).stream()
				.toArray(OrderSpecifier<?>[]::new);
		
		long totalCount = queryFactory
				.select(memberEntity.count())
				.from(memberEntity)
				.where(searchConditions)
				.fetchOne();

		List<MemberEntity> list = queryFactory
				.selectFrom(memberEntity)
				.where(searchConditions)
				.orderBy(orderConditions)
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
	public PostListResponse searchPostList(PostSearchRequest request) {
		
		PostSearchRequest.Condition condition = request.getCondition();

		DateRange createdDate = (condition == null) ? null : condition.getCreatedDate();
		DateRange updatedDate = (condition == null) ? null : condition.getUpdatedDate();
		
		BooleanExpression[] searchConditions = (condition == null) ? null : new BooleanExpression[] {

				QueryDslUtils.equalsIfPresent(postEntity.state, condition.getState()),

				QueryDslUtils.containIfPresent(postEntity.title, condition.getTitle()),
				QueryDslUtils.containIfPresent(memberEntity.username, condition.getWriterUsername()),
				QueryDslUtils.containIfPresent(postEntity.content, condition.getContent()),

				(createdDate == null) ? null 
						: QueryDslUtils.afterIfPresent(postEntity.createdDateTime, createdDate.getFrom()),
				(createdDate == null) ? null 
						: QueryDslUtils.beforeIfPresent(postEntity.createdDateTime, createdDate.getTo()),

				(updatedDate == null) ? null 
						: QueryDslUtils.afterIfPresent(postEntity.createdDateTime, updatedDate.getFrom()),
				(updatedDate == null) ? null 
						: QueryDslUtils.beforeIfPresent(postEntity.createdDateTime, updatedDate.getTo()),
		};
		
		OrderSpecifier<?>[] orderConditions = (request.getSortOrderList() == null) 
				? new OrderSpecifier<?>[] { postEntity.createdDateTime.desc() }
				: request.getSortOrderList().stream().map(x -> {
					 
					PostSort sort = (x.getSort() != null) ? x.getSort() : PostSort.CREATED_DATE_TIME;
					Order order = (x.getOrder() != null) ? x.getOrder() : sort.getDefaultOrder();
					
					ComparableExpressionBase<?> column = sortMap.getOrDefault(sort, memberEntity.username);
					
					return (order == Order.ASC) ? column.asc() : column.desc();
				})
				.collect(Collectors.toCollection(LinkedHashSet::new)).stream()
				.toArray(OrderSpecifier<?>[]::new);
				
		long totalCount = queryFactory
				.select(postEntity.count())
				.from(postEntity)
				.leftJoin(memberEntity)
					.on(postEntity.writerId.eq(memberEntity.id))
				.where(searchConditions)
				.fetchOne();		

		List<Tuple> list = queryFactory
				.select(postEntity, memberEntity)
				.from(postEntity)
				.leftJoin(memberEntity)
					.on(postEntity.writerId.eq(memberEntity.id))
				.where(searchConditions)
				.orderBy(orderConditions)
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
