package com.jong.msa.board.microservice.post.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.client.core.exception.FeignServiceException;
import com.jong.msa.board.client.member.feign.MemberFeignClient;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.client.post.request.CreatePostRequest;
import com.jong.msa.board.client.post.request.ModifyPostRequest;
import com.jong.msa.board.client.post.response.PostDetailsResponse;
import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.common.enums.State;
import com.jong.msa.board.core.web.response.ErrorResponse;
import com.jong.msa.board.domain.post.entity.PostEntity;
import com.jong.msa.board.domain.post.repository.PostRepository;
import com.jong.msa.board.microservice.post.service.PostService;
import com.jong.msa.board.microservice.post.test.consumer.PostMicroserviceTestConsumer;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(
		ports = 9092, partitions = 1, 
		brokerProperties = "listeners=PLAINTEXT://localhost:9092")
public class PostMicroserviceTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@SpyBean
	PostMicroserviceTestConsumer consumer; 

	@SpyBean
	PostService service; 
	
	@MockBean
	MemberFeignClient memberFeignClient;
	
	@Autowired
	PostRepository repository;
	
	@Test
	void 게시글_생성_테스트() throws Exception {

		UUID writerId = UUID.randomUUID();
		UUID notExistsWriterId = UUID.randomUUID();
		
		when(memberFeignClient.getMember(eq(writerId))).thenReturn(
				ResponseEntity.status(HttpStatus.OK)
					.body(MemberDetailsResponse.builder()
							.id(writerId)
							.build()));

		when(memberFeignClient.getMember(eq(notExistsWriterId))).thenThrow(
				new FeignServiceException(
						ErrorCode.NOT_FOUND_MEMBER.getStatusCode(), 
						new HashMap<>(), 
						ErrorResponse.builder()
						.errorCode(ErrorCode.NOT_FOUND_MEMBER)
						.build()));

		CreatePostRequest successRequest = CreatePostRequest.builder()
				.title("title")
				.content("content")
				.writerId(writerId)
				.build();

		CreatePostRequest errorRequest = CreatePostRequest.builder()
				.title("title")
				.content("content")
				.writerId(notExistsWriterId)
				.build();

		mockMvc.perform(post("/apis/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(successRequest)))
			.andExpect(status().isCreated());

		mockMvc.perform(post("/apis/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(errorRequest)))
			.andExpect(status().isGone())
			.andDo(print());

		verify(service, times(2)).createPost(any());
		verify(consumer, timeout(5000)).consumePostSaveTopic(any());
	}
	
	@Test
	void 게시글_수정_테스트() throws Exception {
		
		PostEntity entity = repository.save(PostEntity.builder()
				.title("title")
				.content("content")
				.writerId(UUID.randomUUID())
				.build());
		
		ModifyPostRequest request = ModifyPostRequest.builder()
				.title("update-title")
				.content("update-content")
				.state(State.DEACTIVE)
				.build();
		
		String cachingKey = RedisKeyPrefixes.POST_KEY + entity.getId();

		redisTemplate.opsForValue().set(cachingKey, "test");
		
		mockMvc.perform(patch("/apis/posts/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
	
		PostEntity updatedEntity = repository.findById(entity.getId()).orElseThrow(EntityNotFoundException::new);
		
		verify(service, times(1)).modifyPost(eq(entity.getId()), any());
		verify(consumer, timeout(5000)).consumePostSaveTopic(any());
		
		assertFalse(redisTemplate.hasKey(cachingKey));
		
		assertEquals(updatedEntity.getTitle(), request.getTitle());
		assertEquals(updatedEntity.getContent(), request.getContent());
		assertEquals(updatedEntity.getState(), request.getState());
	}

	@Test
	void 게시글_조회수_증가_테스트() throws Exception {
		
		PostEntity entity = repository.save(PostEntity.builder()
				.title("title")
				.content("content")
				.writerId(UUID.randomUUID())
				.build());
		
		String cachingKey = RedisKeyPrefixes.POST_KEY + entity.getId();

		redisTemplate.opsForValue().set(cachingKey, "test");
		
		mockMvc.perform(patch("/apis/posts/" + entity.getId() + "/views/increase"))
			.andExpect(status().isNoContent());
	
		PostEntity updatedEntity = repository.findById(entity.getId()).orElseThrow(EntityNotFoundException::new);
		
		verify(service, times(1)).increasePostViews(eq(entity.getId()));
		verify(consumer, timeout(5000)).consumePostSaveTopic(any());

		assertFalse(redisTemplate.hasKey(cachingKey));

		assertEquals(updatedEntity.getViews(), entity.getViews() + 1);
	}
	
	@Test
	void 게시글_조회_테스트() throws Exception {
		
		UUID writerId = UUID.randomUUID();
		
		PostEntity entity = repository.save(PostEntity.builder()
				.title("title")
				.content("content")
				.writerId(writerId)
				.build());
		
		when(memberFeignClient.getMember(eq(writerId))).thenReturn(ResponseEntity
				.status(HttpStatus.OK)
				.body(MemberDetailsResponse.builder()
						.id(writerId)
						.build()));
		
		MvcResult firstResult = mockMvc.perform(get("/apis/posts/" + entity.getId())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		MvcResult secondResult = mockMvc.perform(get("/apis/posts/" + entity.getId())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
		
//		PostDetailsResponse post = objectMapper.readValue(
//				firstResult.getResponse().getContentAsString(), PostDetailsResponse.class);
		
		PostDetailsResponse firstPost = objectMapper.readValue(
				firstResult.getResponse().getContentAsString(), PostDetailsResponse.class);
		PostDetailsResponse secondPost = objectMapper.readValue(
				secondResult.getResponse().getContentAsString(), PostDetailsResponse.class);

		verify(service, times(1)).getPost(eq(entity.getId()));

		assertEquals(firstPost.getId(), secondPost.getId());
		assertEquals(firstPost.getTitle(), secondPost.getTitle());
		assertEquals(firstPost.getContent(), secondPost.getContent());
		assertEquals(firstPost.getWriter().getId(), secondPost.getWriter().getId());

		assertEquals(firstPost.getId(), entity.getId());
		assertEquals(firstPost.getTitle(), entity.getTitle());
		assertEquals(firstPost.getContent(), entity.getContent());
		assertEquals(firstPost.getWriter().getId(), entity.getWriterId());
	}
	
}
