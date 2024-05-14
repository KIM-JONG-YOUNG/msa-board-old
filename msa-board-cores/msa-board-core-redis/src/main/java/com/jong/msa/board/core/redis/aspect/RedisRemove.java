package com.jong.msa.board.core.redis.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisRemove {

	String prefix();

	String key();

	@Aspect
	@Component
	@RequiredArgsConstructor
	public static class Advice {

		private final RedisTemplate<String, String> redisTemplate;
		
		private String getCachingKey(String[] paramNames, Object[] paramValues, RedisRemove annotation) {

			StandardEvaluationContext context = new StandardEvaluationContext();
			SpelExpressionParser parser = new SpelExpressionParser();
			
			for (int i = 0; i < paramNames.length; i++) {

				context.setVariable(paramNames[i], paramValues[i]);
			}
			
			String key = annotation.key();
			String prefix = annotation.prefix();
			
			Object parseValue = parser.parseExpression(key).getValue(context); 
			
			return new StringBuilder(prefix).append(parseValue).toString();
		}
		
		@AfterReturning("@annotation(com.jong.msa.board.core.redis.aspect.RedisRemove)")
		public void afterReturning(JoinPoint joinPoint) throws Throwable {

			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			RedisRemove annotation = signature.getMethod().getAnnotation(RedisRemove.class);

			String[] paramNames = signature.getParameterNames();
			Object[] paramValues = joinPoint.getArgs();

			redisTemplate.delete(getCachingKey(paramNames, paramValues, annotation));
		}
	}
	
}
