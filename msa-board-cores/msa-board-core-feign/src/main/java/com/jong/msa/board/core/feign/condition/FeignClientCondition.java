
package com.jong.msa.board.core.feign.condition;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * spring.application.name 과 @FeignClient 의 name 이 다를 경우 생성하는 조건 
 */
public class FeignClientCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

		String springApplicationName = context.getEnvironment().getProperty("spring.application.name");
		
		MergedAnnotation<FeignClient> annotation = metadata.getAnnotations().get(FeignClient.class);

		return !springApplicationName.equalsIgnoreCase(annotation.getString("name"));
	}

}
