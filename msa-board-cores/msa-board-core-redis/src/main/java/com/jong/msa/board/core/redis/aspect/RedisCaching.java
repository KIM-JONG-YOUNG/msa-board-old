package com.jong.msa.board.core.redis.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCaching {

	String prefix();

	String key();

	long cachingTime() default 60;

	@Aspect
	@Component
	@RequiredArgsConstructor
	public static class Advice {

		private final ObjectMapper objectMapper;
		
		private final RedisTemplate<String, String> redisTemplate;
		
		private String getCachingKey(String[] paramNames, Object[] paramValues, RedisCaching annotation) {

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
		
		@Around("@annotation(com.jong.msa.board.core.redis.aspect.RedisCaching)")
		public Object processAround(ProceedingJoinPoint joinPoint) throws Throwable {

			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			RedisCaching annotation = signature.getMethod().getAnnotation(RedisCaching.class);

			Class<?> returnType = signature.getReturnType();

			// 리턴값이 void 일 경우 캐싱에서 조회하지 않고 무조건 실행 
			if (returnType == void.class) {
				
				return joinPoint.proceed();
				
			} else {

				String[] paramNames = signature.getParameterNames();
				Object[] paramValues = joinPoint.getArgs();

				String cachingKey = getCachingKey(paramNames, paramValues, annotation);
				String cachingVal = redisTemplate.opsForValue().get(cachingKey);
				long cachingTime = annotation.cachingTime();

				if (StringUtils.hasText(cachingVal)) {
					
					return objectMapper.readValue(cachingVal, returnType);
				
				} else {

					Object result = joinPoint.proceed();
					
					// Redis Caching 
					if (result != null) {
						
						cachingVal = objectMapper.writeValueAsString(result);
						
						redisTemplate.opsForValue().set(cachingKey, cachingVal, cachingTime, TimeUnit.SECONDS);
					}
					
					return result;
				}
			}
		}
	}
	
}
