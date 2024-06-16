package com.jong.msa.board.microservice.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.client.search.enums.MemberSort;
import com.jong.msa.board.client.search.enums.Order;
import com.jong.msa.board.client.search.enums.PostSort;
import com.jong.msa.board.client.search.enums.SortEnum;
import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.client.search.request.param.DateRange;
import com.jong.msa.board.client.search.request.param.SortOrder;
import com.jong.msa.board.client.search.response.MemberListResponse;
import com.jong.msa.board.client.search.response.PostListResponse;
import com.jong.msa.board.core.persist.utils.QueryDslUtils;
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
		
		BooleanExpression[] searchCondition = null;
		OrderSpecifier<?> orderCondition = null;

		SearchMemberRequest.Condition condition = request.getCondition();

		if (condition != null) {
			
			List<BooleanExpression> conditionList = new ArrayList<>();

			conditionList.add(QueryDslUtils.equalsIfPresent(memberEntity.gender, condition.getGender()));
			conditionList.add(QueryDslUtils.equalsIfPresent(memberEntity.group, condition.getGroup()));
			conditionList.add(QueryDslUtils.equalsIfPresent(memberEntity.state, condition.getState()));

			conditionList.add(QueryDslUtils.containIfPresent(memberEntity.username, condition.getUsername()));
			conditionList.add(QueryDslUtils.containIfPresent(memberEntity.name, condition.getName()));
			conditionList.add(QueryDslUtils.containIfPresent(memberEntity.email, condition.getEmail()));

			DateRange createdDate = condition.getCreatedDate();
			DateRange updatedDate = condition.getUpdatedDate();
			
			if (createdDate != null) {
				
				conditionList.add(QueryDslUtils.afterIfPresent(memberEntity.createdDateTime, createdDate.getFrom()));
				conditionList.add(QueryDslUtils.beforeIfPresent(memberEntity.createdDateTime, createdDate.getTo()));
			}

			if (updatedDate != null) {
				
				conditionList.add(QueryDslUtils.afterIfPresent(memberEntity.updatedDateTime, updatedDate.getFrom()));
				conditionList.add(QueryDslUtils.beforeIfPresent(memberEntity.updatedDateTime, updatedDate.getTo()));
			}

			searchCondition = conditionList.toArray(new BooleanExpression[0]);
			
		} else {
			
			searchCondition = new BooleanExpression[] {};
		}
		
		SortOrder<MemberSort> sortOrder = request.getSortOrder();
		
		if (sortOrder != null) {
			
			MemberSort sort = sortOrder.getSort();
			Order order = (sortOrder.getOrder() != null) 
					? sortOrder.getOrder()
					: sort.getDefaultOrder();

			ComparableExpressionBase<?> column = sortMap.get(sort);

			orderCondition = (order == Order.ASC) ? column.asc() : column.desc();
			
		} else {

			orderCondition = memberEntity.username.asc();
		}
		
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
		
		BooleanExpression[] searchCondition = null;
		OrderSpecifier<?> orderCondition = null;

		SearchPostRequest.Condition condition = request.getCondition();

		if (condition != null) {
			
			List<BooleanExpression> conditionList = new ArrayList<>();

			conditionList.add(QueryDslUtils.equalsIfPresent(postEntity.state, condition.getState()));

			conditionList.add(QueryDslUtils.containIfPresent(postEntity.title, condition.getTitle()));
			conditionList.add(QueryDslUtils.containIfPresent(memberEntity.username, condition.getWriterUsername()));
			conditionList.add(QueryDslUtils.containIfPresent(postEntity.content, condition.getContent()));

			DateRange createdDate = condition.getCreatedDate();
			DateRange updatedDate = condition.getUpdatedDate();
			
			if (createdDate != null) {
				
				conditionList.add(QueryDslUtils.afterIfPresent(memberEntity.createdDateTime, createdDate.getFrom()));
				conditionList.add(QueryDslUtils.beforeIfPresent(memberEntity.createdDateTime, createdDate.getTo()));
			}

			if (updatedDate != null) {
				
				conditionList.add(QueryDslUtils.afterIfPresent(memberEntity.updatedDateTime, updatedDate.getFrom()));
				conditionList.add(QueryDslUtils.beforeIfPresent(memberEntity.updatedDateTime, updatedDate.getTo()));
			}

			searchCondition = conditionList.toArray(new BooleanExpression[0]);
		
		} else {
			
			searchCondition = new BooleanExpression[] {};
		}
		
		SortOrder<PostSort> sortOrder = request.getSortOrder();
		
		if (sortOrder != null) {
			
			PostSort sort = sortOrder.getSort();
			Order order = (sortOrder.getOrder() != null) 
					? sortOrder.getOrder()
					: sort.getDefaultOrder();

			ComparableExpressionBase<?> column = sortMap.get(sort);

			orderCondition = (order == Order.ASC) ? column.asc() : column.desc();
			
		} else {

			orderCondition = postEntity.createdDateTime.desc();
		}
		
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
