package com.jong.msa.board.microservice.search.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import com.jong.msa.board.client.search.request.SearchMemberRequest;
import com.jong.msa.board.client.search.request.SearchPostRequest;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.core.web.validator.RequestValidator;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class SearchRequestValidator extends RequestValidator {

    @Pointcut("args(request, ..) || args(.., request)")
    protected void searchMemberRequestMethod(SearchMemberRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void searchPostRequestMethod(SearchPostRequest request) {}

    @Before("restControllerMethods() && searchMemberRequestMethod(request)")
	public void validateSearchMemberRequest(SearchMemberRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();
    	
    	if (request.getCreatedDateFrom() != null && request.getCreatedDateTo() != null) {

    		errorList.add(validateField("createdDateFrom", request, 
    				createValidEntry(
    						x -> x.getCreatedDateFrom().isAfter(x.getCreatedDateTo()), 
    						"생성 일자 검색 시작 일자는 검색 종료 일자 이후 일 수 없습니다.")));
    	}

    	if (request.getUpdatedDateFrom() != null && request.getUpdatedDateTo() != null) {

    		errorList.add(validateField("updatedDateFrom", request, 
    				createValidEntry(
    						x -> x.getUpdatedDateFrom().isAfter(x.getUpdatedDateTo()), 
    						"수정 일자 검색 시작 일자는 검색 종료 일자 이후 일 수 없습니다.")));
    	}

    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
    }

    @Before("restControllerMethods() && searchPostRequestMethod(request)")
	public void validateSearchPostRequest(SearchPostRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();
    	
    	if (request.getCreatedDateFrom() != null && request.getCreatedDateTo() != null) {

    		errorList.add(validateField("createdDateFrom", request, 
    				createValidEntry(
    						x -> x.getCreatedDateFrom().isAfter(x.getCreatedDateTo()), 
    						"생성 일자 검색 시작 일자는 검색 종료 일자 이후 일 수 없습니다.")));
    	}

    	if (request.getUpdatedDateFrom() != null && request.getUpdatedDateTo() != null) {

    		errorList.add(validateField("updatedDateFrom", request, 
    				createValidEntry(
    						x -> x.getUpdatedDateFrom().isAfter(x.getUpdatedDateTo()), 
    						"수정 일자 검색 시작 일자는 검색 종료 일자 이후 일 수 없습니다.")));
    	}

    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
    }

}
