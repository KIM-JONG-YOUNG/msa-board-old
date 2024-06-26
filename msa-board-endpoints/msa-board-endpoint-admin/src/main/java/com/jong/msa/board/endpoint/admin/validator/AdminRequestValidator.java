package com.jong.msa.board.endpoint.admin.validator;

import java.util.regex.Pattern;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jong.msa.board.client.search.request.DateRange;
import com.jong.msa.board.common.constants.Patterns;
import com.jong.msa.board.core.web.validator.RequestValidator;
import com.jong.msa.board.endpoint.admin.request.AdminLoginRequest;
import com.jong.msa.board.endpoint.admin.request.AdminMemberSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPasswordModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostModifyRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostSearchRequest;
import com.jong.msa.board.endpoint.admin.request.AdminPostWriteRequest;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class AdminRequestValidator extends RequestValidator {

    @Pointcut("args(request, ..) || args(.., request)")
    protected void adminLoginRequestMethod(AdminLoginRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void adminModifyRequestMethod(AdminModifyRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void adminPasswordModifyRequestMethod(AdminPasswordModifyRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void adminMemberSearchRequestMethod(AdminMemberSearchRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void adminPostWriteRequestMethod(AdminPostWriteRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void adminPostModifyRequestMethod(AdminPostModifyRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void adminPostSearchRequestMethod(AdminPostSearchRequest request) {}

    @Before("restControllerMethods() && adminLoginRequestMethod(request)")
	public void validate(AdminLoginRequest request) {

		validateRequest(
				
				validateField("username", request.getUsername(),
						validation(x -> !StringUtils.hasText(x), "계정은 비어있을 수 없습니다."),
						validation(x -> x.length() > 30, "계정이 30자를 초과할 수 없습니다."),
						validation(x -> !Pattern.matches(Patterns.USERNAME_PATTERN, x), "계정이 형식에 맞지 않습니다.")),

				validateField("password", request.getPassword(),
						validation(x -> !StringUtils.hasText(x), "비밀번호는 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && adminModifyRequestMethod(request)")
	public void validate(AdminModifyRequest request) {

		validateRequest(
				
				(request.getName() == null) ? null
						: validateField("name", request.getName(),
								validation(x -> !StringUtils.hasText(x), "이름은 비어있을 수 없습니다."),
								validation(x -> x.length() > 30, "이름이 30자를 초과할 수 없습니다.")),
						
				(request.getEmail() == null) ? null
						: validateField("email", request.getEmail(),
								validation(x -> !StringUtils.hasText(x), "이메일은 비어있을 수 없습니다."),
								validation(x -> x.length() > 30, "이메일은 30자를 초과할 수 없습니다."),
								validation(x -> !Pattern.matches(Patterns.EMAIL_PATTERN, x), "이메일이 형식에 맞지 않습니다.")));
    }

    @Before("restControllerMethods() && adminPasswordModifyRequestMethod(request)")
	public void validate(AdminPasswordModifyRequest request) {

		validateRequest(
				
				validateField("currentPassword", request.getCurrentPassword(),
						validation(x -> !StringUtils.hasText(x), "새로운 비밀번호는 비어있을 수 없습니다.")),
				
				validateField("newPassword", request.getNewPassword(),
						validation(x -> !StringUtils.hasText(x), "현재 비밀번호는 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && adminMemberSearchRequestMethod(request)")
	public void validate(AdminMemberSearchRequest request) {

    	AdminMemberSearchRequest.Condition condition = request.getCondition();
    	
    	DateRange createdDate = (condition == null) ? null : condition.getCreatedDate();
    	DateRange updatedDate = (condition == null) ? null : condition.getUpdatedDate();
    	
    	validateRequest(
    			
    			validateField("offset", request.getOffset(), 
    					validation(x -> x < 0, "조회 시작 행은 0보다 작을 수 없습니다.")),

    			validateField("limit", request.getLimit(), 
    					validation(x -> x < 0, "조회 건 수는 0보다 작을 수 없습니다.")),

    			(createdDate == null || createdDate.getFrom() == null || createdDate.getTo() == null) ? null
    					: validateField("condition.createdDate", createdDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")),
    			
				(updatedDate == null || updatedDate.getFrom() == null || updatedDate.getTo() == null) ? null
    					: validateField("condition.updatedDate", updatedDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")));
    }

    @Before("restControllerMethods() && adminPostWriteRequestMethod(request)")
	public void validate(AdminPostWriteRequest request) {

    	validateRequest(
    			
    			validateField("title", request.getTitle(), 
    					validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
    					validation(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")),
    			
    			validateField("content", request.getContent(), 
    					validation(x -> !StringUtils.hasText(x), "내용은 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && adminPostModifyRequestMethod(request)")
	public void validate(AdminPostModifyRequest request) {

    	validateRequest(
    			
    			(request.getTitle() == null) ? null 
    					: validateField("title", request.getTitle(), 
    							validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
    							validation(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")),
    			
				(request.getTitle() == null) ? null 
						: validateField("content", request.getContent(), 
								validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && adminPostSearchRequestMethod(request)")
	public void validate(AdminPostSearchRequest request) {

    	AdminPostSearchRequest.Condition condition = request.getCondition();
    	
    	DateRange createdDate = (condition == null) ? null : condition.getCreatedDate();
    	DateRange updatedDate = (condition == null) ? null : condition.getUpdatedDate();
    	
    	validateRequest(
    			
    			validateField("offset", request.getOffset(), 
    					validation(x -> x < 0, "조회 시작 행은 0보다 작을 수 없습니다.")),

    			validateField("limit", request.getLimit(), 
    					validation(x -> x < 0, "조회 건 수는 0보다 작을 수 없습니다.")),

    			(createdDate == null || createdDate.getFrom() == null || createdDate.getTo() == null) ? null
    					: validateField("condition.createdDate", createdDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")),
    			
				(updatedDate == null || updatedDate.getFrom() == null || updatedDate.getTo() == null) ? null
    					: validateField("condition.updatedDate", updatedDate, 
    							validation(x -> x.getFrom().isAfter(x.getTo()), "검색 시작 일자는 검색 종료 일자 보다 이전일 수 없습니다.")));
    }

}
