package com.jong.msa.board.core.transaction.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisTimeoutException;
import org.redisson.client.RedisTryAgainException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.RequiredArgsConstructor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeTransaction {

	String prefix();

	String key();

	long waitTime() default 5;

	long leaseTime() default 3;

	@Aspect
	@Component
	@RequiredArgsConstructor 
	public static class Advice {

		private final RedissonClient redissonClient;

		private final TransactionTemplate transactionTemplate;

		private String getLockName(String[] paramNames, Object[] paramValues, DistributeTransaction annotation) {

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
		
		@Around("@annotation(com.jong.msa.board.core.transaction.aspect.DistributeTransaction)")
		public Object processAround(ProceedingJoinPoint joinPoint) throws Throwable {

			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			DistributeTransaction annotation = signature.getMethod().getAnnotation(DistributeTransaction.class);

			String[] paramNames = signature.getParameterNames();
			Object[] paramValues = joinPoint.getArgs();

			String lockName = getLockName(paramNames, paramValues, annotation);
			
			AtomicReference<Object> transactionResult = new AtomicReference<>();
			AtomicReference<Throwable> transactionException = new AtomicReference<>();

			RLock redissonLock = redissonClient.getLock(lockName);

			long waitTime = annotation.waitTime();
			long leaseTime = annotation.leaseTime();

			if (redissonLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {

				transactionTemplate.executeWithoutResult(status -> {
					
					try {
						transactionResult.set(joinPoint.proceed());
					} catch (Throwable e) {
						transactionException.set(e);
					} finally {
						if (!redissonLock.isLocked()) {	// Redisson Lock 이 해제되었을 경우 
							transactionException.set(new RedisTimeoutException(
									String.format("Redisson Lock 실행 시간을 초과하였습니다. (lockName: %s)", lockName)));
						}
					}

					if (transactionException.get() != null) {
						status.setRollbackOnly();
					}
				});

				if (redissonLock.isLocked()) {
					redissonLock.unlock();
				}

				if (transactionException.get() != null) {	
					throw transactionException.get();	// 오류가 존재할 경우 오류 발생 
				} else {									
					return transactionResult.get();
				}
			} else {
				
				throw new RedisTryAgainException(
						String.format("Redisson Lock를 획득하는데 오류가 발생했습니다. (lockName: %s)", lockName));
			}
		}
	}

}
