package com.jong.msa.board.core.feign.test.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.jong.msa.board.core.feign.condition.FeignClientCondition;

@Conditional(FeignClientCondition.class)
@FeignClient(name = "test-application-test", url = "http://localhost:8080")
public interface FeignCoreTestCreatedFeignClient {

	@GetMapping(value = "/test")
	ResponseEntity<Void> test();
	
}
