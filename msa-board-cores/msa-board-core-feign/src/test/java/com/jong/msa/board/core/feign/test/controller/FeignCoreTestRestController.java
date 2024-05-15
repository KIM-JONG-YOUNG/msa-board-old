package com.jong.msa.board.core.feign.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jong.msa.board.core.feign.test.client.FeignCoreTestCreatedFeignClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FeignCoreTestRestController {

	private final FeignCoreTestCreatedFeignClient client;

	@GetMapping(value = "/client/error")
	ResponseEntity<Void> clientErrorTest() {

		client.test();

		return ResponseEntity.noContent().build();
	}

}
