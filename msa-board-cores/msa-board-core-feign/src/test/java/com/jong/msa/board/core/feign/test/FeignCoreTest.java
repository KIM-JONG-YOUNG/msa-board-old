package com.jong.msa.board.core.feign.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.jong.msa.board.core.feign.exception.FeignServiceException;
import com.jong.msa.board.core.feign.test.client.FeignCoreTestCreatedFeignClient;
import com.jong.msa.board.core.feign.test.client.FeignCoreTestNotCreatedTestFeignClient;
import com.jong.msa.board.core.web.response.ErrorResponse;

import feign.codec.ErrorDecoder;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8080)
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class, 
		classes = FeignCoreTestContext.class)
public class FeignCoreTest {
 
	@Autowired  
	MockMvc mockMvc;
	
	@Autowired
	ApplicationContext context;

	@Autowired
	ObjectMapper objectMapper;
	
	@SpyBean
	ErrorDecoder errorDecoder;
	
	@Test
	void FiegnClientCondition_따른_Bean_생성_테스트() {

		assertNotNull(context.getBean(FeignCoreTestCreatedFeignClient.class));
		assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(FeignCoreTestNotCreatedTestFeignClient.class));
	}
	
	@Test
	void FiegnException_예외_핸들링_테스트() throws Exception {
		
		String errorResponseJson = objectMapper.writeValueAsString(ErrorResponse.builder().build());
		
		WireMock.stubFor(WireMock.get(WireMock.anyUrl())
				.willReturn(WireMock.jsonResponse(errorResponseJson, 400)));
		
		MvcResult result = mockMvc.perform(get("/client/error"))
			.andDo(print())
			.andReturn();

		verify(errorDecoder, times(1)).decode(any(), any());
		
		assertEquals(FeignServiceException.class, result.getResolvedException().getClass());
	}

}
