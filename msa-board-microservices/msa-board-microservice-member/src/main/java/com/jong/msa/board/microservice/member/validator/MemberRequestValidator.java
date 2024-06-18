package com.jong.msa.board.microservice.member.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;

import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.common.constants.Patterns;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.exception.RestServiceException;
import com.jong.msa.board.core.web.validator.RequestValidator;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class MemberRequestValidator extends RequestValidator {

    @Pointcut("args(request, ..) || args(.., request)")
    protected void createMemberRequestMethod(CreateMemberRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void modifyMemberRequestMethod(ModifyMemberRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void modifyMemberPasswordRequestMethod(ModifyMemberPasswordRequest request) {}

    @Pointcut("args(request, ..) || args(.., request)")
    protected void loginMemberRequestMethod(LoginMemberRequest request) {}

    @Before("restControllerMethods() && createMemberRequestMethod(request)")
	public void validateCreateMemberRequest(CreateMemberRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();
    	
    	errorList.add(validateField("username", request.getUsername(), 
				createValidEntry(x -> !StringUtils.hasText(x), "계정은 비어있을 수 없습니다."), 
				createValidEntry(x -> x.length() > 30, "계정이 30자를 초과할 수 없습니다."),
				createValidEntry(x -> !Pattern.matches(Patterns.USERNAME_PATTERN, x), "계정이 형식에 맞지 않습니다.")));
    	
    	errorList.add(validateField("password", request.getPassword(), 
				createValidEntry(x -> !StringUtils.hasText(x), "비밀번호는 비어있을 수 없습니다.")));

    	errorList.add(validateField("name", request.getName(), 
				createValidEntry(x -> !StringUtils.hasText(x), "이름은 비어있을 수 없습니다."), 
				createValidEntry(x -> x.length() > 30, "이름이 30자를 초과할 수 없습니다.")));

    	errorList.add(validateField("gender", request.getGender(), 
				createValidEntry(x -> x == null, "성별은 비어있을 수 없습니다.")));

    	errorList.add(validateField("email", request.getEmail(), 
				createValidEntry(x -> !StringUtils.hasText(x), "이메일은 비어있을 수 없습니다."), 
				createValidEntry(x -> x.length() > 30, "이메일은 30자를 초과할 수 없습니다."),
				createValidEntry(x -> !Pattern.matches(Patterns.EMAIL_PATTERN, x), "이메일이 형식에 맞지 않습니다.")));

    	errorList.add(validateField("group", request.getGroup(), 
				createValidEntry(x -> x == null, "그룹은 비어있을 수 없습니다.")));

    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
	}
    
    @Before("restControllerMethods() && modifyMemberRequestMethod(request)")
	public void validateModifyMemberRequest(ModifyMemberRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();
    	
		if (request.getName() != null) {

			errorList.add(validateField("name", request.getName(), 
					createValidEntry(x -> !StringUtils.hasText(x), "이름은 비어있을 수 없습니다."), 
					createValidEntry(x -> x.length() > 30, "이름이 30자를 초과할 수 없습니다.")));
		}

		if (request.getName() != null) {

			errorList.add(validateField("email", request.getEmail(), 
					createValidEntry(x -> !StringUtils.hasText(x), "이메일은 비어있을 수 없습니다."), 
					createValidEntry(x -> x.length() > 30, "이메일은 30자를 초과할 수 없습니다."),
					createValidEntry(x -> !Pattern.matches(Patterns.EMAIL_PATTERN, x), "이메일이 형식에 맞지 않습니다.")));
		}
		
    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
	}
	
    @Before("restControllerMethods() && modifyMemberPasswordRequestMethod(request)")
	public void validateModifyMemberPasswordRequest(ModifyMemberPasswordRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();
    	
    	errorList.add(validateField("currentPassword", request.getCurrentPassword(), 
				createValidEntry(x -> !StringUtils.hasText(x), "새로운 비밀번호는 비어있을 수 없습니다.")));

    	errorList.add(validateField("newPassword", request.getNewPassword(), 
				createValidEntry(x -> !StringUtils.hasText(x), "현재 비밀번호는 비어있을 수 없습니다.")));

    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
	}
    
    @Before("restControllerMethods() && loginMemberRequestMethod(request)")
	public void validateLoginMemberRequest(LoginMemberRequest request) {

    	List<ObjectError> errorList = new ArrayList<>();
    	
    	errorList.add(validateField("username", request.getUsername(), 
				createValidEntry(x -> !StringUtils.hasText(x), "계정은 비어있을 수 없습니다."), 
				createValidEntry(x -> x.length() > 30, "계정이 30자를 초과할 수 없습니다."),
				createValidEntry(x -> !Pattern.matches(Patterns.USERNAME_PATTERN, x), "계정이 형식에 맞지 않습니다.")));
    	
    	errorList.add(validateField("password", request.getPassword(), 
				createValidEntry(x -> !StringUtils.hasText(x), "비밀번호는 비어있을 수 없습니다.")));
		
    	errorList = errorList.stream().filter(x -> x != null).collect(Collectors.toList());
    	
    	if (errorList.size() > 0) {
    		
    		throw new RestServiceException(ErrorCode.INVALID_PARAMETER, errorList);
    	}
	}
    
}
