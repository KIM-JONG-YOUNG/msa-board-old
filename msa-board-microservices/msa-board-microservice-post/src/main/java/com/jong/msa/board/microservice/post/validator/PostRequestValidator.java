package com.jong.msa.board.microservice.post.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.core.web.validator.RequestValidator;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class PostRequestValidator extends RequestValidator {

    @Pointcut("args(request, ..) || args(.., request)")
    protected void createPostRequestMethod(CreatePostRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void modifyPostRequestMethod(ModifyPostRequest request) {}

    @Before("restControllerMethods() && createPostRequestMethod(request)")
	public void validateCreateMemberRequest(CreatePostRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();
    	
    	errorList.add(validateField("title", request.getTitle(), 
				createValidEntry(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
				createValidEntry(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")));
    	
    	errorList.add(validateField("content", request.getContent(), 
				createValidEntry(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다.")));

    	errorList.add(validateField("writerId", request.getWriterId(), 
				createValidEntry(x -> x == null, "작성자 ID는 비어있을 수 없습니다.")));

    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
    }

    @Before("restControllerMethods() && modifyPostRequestMethod(request)")
	public void validateCreateMemberRequest(ModifyPostRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();

    	if (request.getTitle() != null) {

        	errorList.add(validateField("title", request.getTitle(), 
    				createValidEntry(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
    				createValidEntry(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")));
    	} 

    	if (request.getContent() != null) {

        	errorList.add(validateField("content", request.getContent(), 
    				createValidEntry(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다.")));
    	}

    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
    }

}
