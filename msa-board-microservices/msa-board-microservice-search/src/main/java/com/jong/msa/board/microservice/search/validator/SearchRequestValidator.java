package com.jong.msa.board.microservice.search.validator;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.jong.msa.board.client.search.request.DateRange;
import com.jong.msa.board.client.search.request.MemberSearchRequest;
import com.jong.msa.board.client.search.request.PostSearchRequest;
import com.jong.msa.board.core.web.validator.RequestValidator;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class SearchRequestValidator extends RequestValidator {

    @Pointcut("args(request, ..) || args(.., request)")
    protected void searchMemberRequestMethod(MemberSearchRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void searchPostRequestMethod(PostSearchRequest request) {}

    @Before("restControllerMethods() && searchMemberRequestMethod(request)")
	public void validate(MemberSearchRequest request) {

    	MemberSearchRequest.Condition condition = request.getCondition();
    	
    	DateRange createdDate = (condition == null) ? null : condition.getCreatedDate();
    	DateRange updatedDate = (condition == null) ? null : condition.getUpdatedDate();
    	
    	validateRequest(
    			
    			(createdDate == null || createdDate.getFrom() == null || createdDate.getTo() == null) ? null
    					: validateField("condition.createdDate", createdDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")),
    			
				(updatedDate == null || updatedDate.getFrom() == null || updatedDate.getTo() == null) ? null
    					: validateField("condition.updatedDate", updatedDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")));
    }

    @Before("restControllerMethods() && searchPostRequestMethod(request)")
	public void validate(PostSearchRequest request) {

    	PostSearchRequest.Condition condition = request.getCondition();

    	DateRange createdDate = (condition == null) ? null : condition.getCreatedDate();
    	DateRange updatedDate = (condition == null) ? null : condition.getUpdatedDate();
    	
    	validateRequest(
    			
    			(createdDate == null || createdDate.getFrom() == null || createdDate.getTo() == null) ? null
    					: validateField("condition.createdDate", createdDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")),
    			
				(updatedDate == null || updatedDate.getFrom() == null || updatedDate.getTo() == null) ? null
    					: validateField("condition.updatedDate", updatedDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")));
    }

}
