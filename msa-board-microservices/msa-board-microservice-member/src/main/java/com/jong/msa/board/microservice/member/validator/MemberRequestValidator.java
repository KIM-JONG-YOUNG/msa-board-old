package com.jong.msa.board.microservice.member.validator;

import java.util.regex.Pattern;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jong.msa.board.client.member.request.MemberCreateRequest;
import com.jong.msa.board.client.member.request.MemberLoginRequest;
import com.jong.msa.board.client.member.request.MemberModifyRequest;
import com.jong.msa.board.client.member.request.MemberPasswordModifyRequest;
import com.jong.msa.board.common.constants.Patterns;
import com.jong.msa.board.core.web.validator.RequestValidator;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class MemberRequestValidator extends RequestValidator {

	@Pointcut("args(request, ..) || args(.., request)")
	protected void createMemberRequestMethod(MemberCreateRequest request) {
	}

	@Pointcut("args(request, ..) || args(.., request)")
	protected void modifyMemberRequestMethod(MemberModifyRequest request) {
	}

	@Pointcut("args(request, ..) || args(.., request)")
	protected void modifyMemberPasswordRequestMethod(MemberPasswordModifyRequest request) {
	}

	@Pointcut("args(request, ..) || args(.., request)")
	protected void loginMemberRequestMethod(MemberLoginRequest request) {
	}

	@Before("restControllerMethods() && createMemberRequestMethod(request)")
	public void validate(MemberCreateRequest request) {

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
						validation(x -> !Pattern.matches(Patterns.EMAIL_PATTERN, x), "이메일이 형식에 맞지 않습니다.")),

				validateField("group", request.getGroup(), 
						validation(x -> x == null, "그룹은 비어있을 수 없습니다.")));
	}

	@Before("restControllerMethods() && modifyMemberRequestMethod(request)")
	public void validate(MemberModifyRequest request) {

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

	@Before("restControllerMethods() && modifyMemberPasswordRequestMethod(request)")
	public void validate(MemberPasswordModifyRequest request) {

		validateRequest(
				
				validateField("currentPassword", request.getCurrentPassword(),
						validation(x -> !StringUtils.hasText(x), "새로운 비밀번호는 비어있을 수 없습니다.")),
				
				validateField("newPassword", request.getNewPassword(),
						validation(x -> !StringUtils.hasText(x), "현재 비밀번호는 비어있을 수 없습니다.")));
	}

	@Before("restControllerMethods() && loginMemberRequestMethod(request)")
	public void validate(MemberLoginRequest request) {
		
		validateRequest(
				
				validateField("username", request.getUsername(),
						validation(x -> !StringUtils.hasText(x), "계정은 비어있을 수 없습니다."),
						validation(x -> x.length() > 30, "계정이 30자를 초과할 수 없습니다."),
						validation(x -> !Pattern.matches(Patterns.USERNAME_PATTERN, x), "계정이 형식에 맞지 않습니다.")),

				validateField("password", request.getPassword(),
						validation(x -> !StringUtils.hasText(x), "비밀번호는 비어있을 수 없습니다.")));
	}

}
