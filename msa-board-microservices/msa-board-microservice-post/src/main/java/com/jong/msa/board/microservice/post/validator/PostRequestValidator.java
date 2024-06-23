package com.jong.msa.board.microservice.post.validator;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jong.msa.board.client.post.request.PostCreateRequest;
import com.jong.msa.board.client.post.request.PostModifyRequest;
import com.jong.msa.board.core.web.validator.RequestValidator;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class PostRequestValidator extends RequestValidator {

    @Pointcut("args(request, ..) || args(.., request)")
    protected void createPostRequestMethod(PostCreateRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void modifyPostRequestMethod(PostModifyRequest request) {}

    @Before("restControllerMethods() && createPostRequestMethod(request)")
	public void validate(PostCreateRequest request) {

    	validateRequest(
    			
    			validateField("title", request.getTitle(), 
    					validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
    					validation(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")),
    			
    			validateField("content", request.getContent(), 
    					validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다.")),
    			
    			validateField("writerId", request.getWriterId(), 
    					validation(x -> x == null, "작성자 ID는 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && modifyPostRequestMethod(request)")
	public void validate(PostModifyRequest request) {

    	validateRequest(
    			
    			(request.getTitle() == null) ? null 
    					: validateField("title", request.getTitle(), 
    							validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
    							validation(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")),
    			
				(request.getTitle() == null) ? null 
						: validateField("content", request.getContent(), 
								validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다.")));
    }

}
