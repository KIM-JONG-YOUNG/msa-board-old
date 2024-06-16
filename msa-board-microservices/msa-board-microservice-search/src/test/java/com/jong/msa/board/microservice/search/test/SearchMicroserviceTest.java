package com.jong.msa.board.microservice.search.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.enums.Gender;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.repository.MemberRepository;
import com.jong.msa.board.domain.post.entity.PostEntity;
import com.jong.msa.board.domain.post.repository.PostRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@EmbeddedKafka(
		ports = 9092, partitions = 1, 
		brokerProperties = "listeners=PLAINTEXT://localhost:9092")
public class SearchMicroserviceTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PostRepository postRepository;
	
	@BeforeAll
	void 데이터_초기화() {

		List<UUID> memberIdList = IntStream.range(0, 10).boxed()
			.map(i -> memberRepository.save(MemberEntity.builder()
					.username("username_" + i)
					.password("password")
					.name("name")
					.email("email")
					.gender(Gender.MAIL)
					.group(Group.ADMIN)
					.build()).getId())
			.collect(Collectors.toList());

		IntStream.range(0, 10).boxed().forEach(
				i -> postRepository.save(PostEntity.builder()
						.title("title_" + i + "_1")
						.content("content")
						.writerId(memberIdList.get(i))
						.build()));
		
		IntStream.range(0, 10).boxed().forEach(
				i -> postRepository.save(PostEntity.builder()
						.title("title_" + i + "_2")
						.content("content")
						.writerId(memberIdList.get(i))
						.build()));
	}
	
	@Test
	void 회원_검색_테스트() throws Exception {

		mockMvc.perform(post("/apis/search/members")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isOk());

	}
	
	@Test
	void 게시글_검색_테스트() throws Exception {

		mockMvc.perform(post("/apis/search/posts")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isOk());
	}
	
}
