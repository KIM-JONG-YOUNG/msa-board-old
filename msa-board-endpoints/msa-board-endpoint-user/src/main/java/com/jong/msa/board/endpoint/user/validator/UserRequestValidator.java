package com.jong.msa.board.endpoint.user.validator;

import java.util.regex.Pattern;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jong.msa.board.client.search.request.DateRange;
import com.jong.msa.board.common.constants.Patterns;
import com.jong.msa.board.core.web.validator.RequestValidator;
import com.jong.msa.board.endpoint.user.request.UserJoinRequest;
import com.jong.msa.board.endpoint.user.request.UserLoginRequest;
import com.jong.msa.board.endpoint.user.request.UserModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPasswordModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostModifyRequest;
import com.jong.msa.board.endpoint.user.request.UserPostSearchRequest;
import com.jong.msa.board.endpoint.user.request.UserPostWriteRequest;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class UserRequestValidator extends RequestValidator {

    @Pointcut("args(request, ..) || args(.., request)")
    protected void userJoinRequestMethod(UserJoinRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void userLoginRequestMethod(UserLoginRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void userModifyRequestMethod(UserModifyRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void userPasswordModifyRequestMethod(UserPasswordModifyRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void userPostWriteRequestMethod(UserPostWriteRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void userPostModifyRequestMethod(UserPostModifyRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void userPostSearchRequestMethod(UserPostSearchRequest request) {}

    @Before("restControllerMethods() && userJoinRequestMethod(request)")
	public void validate(UserJoinRequest request) {
    	
		validateRequest(
				
				validateField("username", request.getUsername(),
						validation(x -> !StringUtils.hasText(x), "계정은 비어있을 수 없습니다."),
						validation(x -> !StringUtils.hasText(x), "계정은 비어있을 수 없습니다."),
						validation(x -> x.length() > 30, "계정이 30자를 초과할 수 없습니다."),
						validation(x -> !Pattern.matches(Patterns.USERNAME_PATTERN, x), "계정이 형식에 맞지 않습니다.")),

				validateField("password", request.getPassword(),
						validation(x -> !StringUtils.hasText(x), "비밀번호는 비어있을 수 없습니다.")),

				validateField("name", request.getName(),
						validation(x -> !StringUtils.hasText(x), "이름은 비어있을 수 없습니다."),
						validation(x -> x.length() > 30, "이름이 30자를 초과할 수 없습니다.")),

				validateField("gender", request.getGender(), validation(x -> x == null, "성별은 비어있을 수 없습니다.")),

				validateField("email", request.getEmail(),
						validation(x -> !StringUtils.hasText(x), "이메일은 비어있을 수 없습니다."),
						validation(x -> x.length() > 30, "이메일은 30자를 초과할 수 없습니다."),
						validation(x -> !Pattern.matches(Patterns.EMAIL_PATTERN, x), "이메일이 형식에 맞지 않습니다.")));
    }

    @Before("restControllerMethods() && userLoginRequestMethod(request)")
	public void validate(UserLoginRequest request) {

		validateRequest(
				
				validateField("username", request.getUsername(),
						validation(x -> !StringUtils.hasText(x), "계정은 비어있을 수 없습니다."),
						validation(x -> x.length() > 30, "계정이 30자를 초과할 수 없습니다."),
						validation(x -> !Pattern.matches(Patterns.USERNAME_PATTERN, x), "계정이 형식에 맞지 않습니다.")),

				validateField("password", request.getPassword(),
						validation(x -> !StringUtils.hasText(x), "비밀번호는 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && userModifyRequestMethod(request)")
	public void validate(UserModifyRequest request) {

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

    @Before("restControllerMethods() && userPasswordModifyRequestMethod(request)")
	public void validate(UserPasswordModifyRequest request) {

		validateRequest(
				
				validateField("currentPassword", request.getCurrentPassword(),
						validation(x -> !StringUtils.hasText(x), "새로운 비밀번호는 비어있을 수 없습니다.")),
				
				validateField("newPassword", request.getNewPassword(),
						validation(x -> !StringUtils.hasText(x), "현재 비밀번호는 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && userPostWriteRequestMethod(request)")
	public void validate(UserPostWriteRequest request) {

    	validateRequest(
    			
    			validateField("title", request.getTitle(), 
    					validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
    					validation(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")),
    			
    			validateField("content", request.getContent(), 
    					validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && userPostModifyRequestMethod(request)")
	public void validate(UserPostModifyRequest request) {

    	validateRequest(
    			
    			(request.getTitle() == null) ? null 
    					: validateField("title", request.getTitle(), 
    							validation(x -> !StringUtils.hasText(x), "제목은 비어있을 수 없습니다."), 
    							validation(x -> x.length() > 300, "제목은 300자를 초과할 수 없습니다.")),
    			
				(request.getTitle() == null) ? null 
						: validateField("content", request.getContent(), 
								validation(x -> !StringUtils.hasText(x), "내용은 비어있을 수 없습니다.")));
    }

    @Before("restControllerMethods() && userPostSearchRequestMethod(request)")
	public void validate(UserPostSearchRequest request) {

    	UserPostSearchRequest.Condition condition = request.getCondition();
    	
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
