package com.jong.msa.board.microservice.member.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.client.member.request.CreateMemberRequest;
import com.jong.msa.board.client.member.request.LoginMemberRequest;
import com.jong.msa.board.client.member.request.ModifyMemberPasswordRequest;
import com.jong.msa.board.client.member.request.ModifyMemberRequest;
import com.jong.msa.board.client.member.response.MemberDetailsResponse;
import com.jong.msa.board.common.constants.RedisKeyPrefixes;
import com.jong.msa.board.common.enums.DBCodeEnum.Gender;
import com.jong.msa.board.common.enums.DBCodeEnum.Group;
import com.jong.msa.board.common.enums.DBCodeEnum.State;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.repository.MemberRepository;
import com.jong.msa.board.microservice.member.service.MemberService;
import com.jong.msa.board.microservice.member.test.consumer.MemberMicroserviceTestConsumer;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(
		ports = 9092, partitions = 1, 
		brokerProperties = "listeners=PLAINTEXT://localhost:9092")
public class MemberMicroserviceTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@SpyBean
	MemberMicroserviceTestConsumer consumer; 

	@SpyBean
	MemberService service; 
	
	@Autowired
	MemberRepository repository;
	
	@Autowired
	PasswordEncoder encoder;

	@Test
	void 회원_생성_테스트() throws Exception {
		
		CreateMemberRequest request = CreateMemberRequest.builder()
				.username("createTester")
				.password("password")
				.name("name")
				.gender(Gender.MAIL)
				.email("test@example.com")
				.group(Group.ADMIN)
				.build();
		
		mockMvc.perform(post("/apis/members")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());

		// 중복 계정으로 인한 오류 발생 
		mockMvc.perform(post("/apis/members")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isConflict());

		verify(service, times(2)).createMember(any());
		verify(consumer, timeout(5000)).consumeMemberSaveTopic(any());
	}

	@Test
	void 회원_수정_테스트() throws Exception {
		
		MemberEntity entity = repository.save(MemberEntity.builder()
				.username("modifyTester")
				.password(encoder.encode("password"))
				.name("name")
				.gender(Gender.MAIL)
				.email("test@example.com")
				.group(Group.ADMIN)
				.build());
		
		ModifyMemberRequest request = ModifyMemberRequest.builder()
				.name("update-name")
				.gender(Gender.FEMAIL)
				.email("update-test@example.com")
				.group(Group.USER)
				.state(State.DEACTIVE)
				.build();

		String cachingKey = RedisKeyPrefixes.MEMBER_KEY + entity.getId();

		// 캐시 데이터 생성 
		redisTemplate.opsForValue().set(cachingKey, "test");

		mockMvc.perform(patch("/apis/members/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());

		MemberEntity updatedEntity = repository.findById(entity.getId()).orElseThrow(EntityNotFoundException::new);
		
		verify(service, times(1)).modifyMember(eq(entity.getId()), any());
		verify(consumer, timeout(5000)).consumeMemberSaveTopic(any());

		// 회원 수정 API를 호출하였기 때문에 캐시에 존재하지 않아야 함 
		assertFalse(redisTemplate.hasKey(cachingKey));

		assertEquals(updatedEntity.getName(), request.getName());
		assertEquals(updatedEntity.getGender(), request.getGender());
		assertEquals(updatedEntity.getEmail(), request.getEmail());
		assertEquals(updatedEntity.getGroup(), request.getGroup());
		assertEquals(updatedEntity.getState(), request.getState());
	}
	
	@Test
	void 회원_비밀번호_수정_테스트() throws Exception {
		
		MemberEntity entity = repository.save(MemberEntity.builder()
				.username("modifyPasswordTester")
				.password(encoder.encode("original-password"))
				.name("name")
				.gender(Gender.MAIL)
				.email("test@example.com")
				.group(Group.ADMIN)
				.build());

		mockMvc.perform(patch("/apis/members/" + entity.getId() + "/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ModifyMemberPasswordRequest.builder()
						.currentPassword("no-original-password")
						.newPassword("update-password")
						.build())))
			.andExpect(status().isBadRequest());

		mockMvc.perform(patch("/apis/members/" + entity.getId() + "/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ModifyMemberPasswordRequest.builder()
						.currentPassword("original-password")
						.newPassword("update-password")
						.build())))
			.andExpect(status().isNoContent());

		MemberEntity updatedEntity = repository.findById(entity.getId()).orElseThrow(EntityNotFoundException::new);
		
		verify(service, times(2)).modifyMemberPassword(eq(entity.getId()), any());
		verify(consumer, timeout(5000)).consumeMemberSaveTopic(any());
		
		assertFalse(encoder.matches("original-password", updatedEntity.getPassword()));
		assertTrue(encoder.matches("update-password", updatedEntity.getPassword()));
	}
	
	@Test
	void 회원_조회_테스트() throws Exception {
		
		MemberEntity entity = repository.save(MemberEntity.builder()
				.username("getMemberTester")
				.password(encoder.encode("password"))
				.name("name")
				.gender(Gender.MAIL)
				.email("test@example.com")
				.group(Group.ADMIN)
				.build());

		MvcResult firstResult = mockMvc.perform(get("/apis/members/" + entity.getId())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		MvcResult secondResult = mockMvc.perform(get("/apis/members/" + entity.getId())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
		
		MemberDetailsResponse firstMember = objectMapper.readValue(
				firstResult.getResponse().getContentAsString(), MemberDetailsResponse.class);
		MemberDetailsResponse secondMember = objectMapper.readValue(
				secondResult.getResponse().getContentAsString(), MemberDetailsResponse.class);

		verify(service, times(1)).getMember(eq(entity.getId()));

		System.out.println(firstResult.getResponse().getContentAsString());
		System.out.println(secondResult.getResponse().getContentAsString());
		
		assertEquals(firstMember.getId(), secondMember.getId());
		assertEquals(firstMember.getUsername(), secondMember.getUsername());
		assertEquals(firstMember.getName(), secondMember.getName());
		assertEquals(firstMember.getGender(), secondMember.getGender());
		assertEquals(firstMember.getEmail(), secondMember.getEmail());
		assertEquals(firstMember.getGroup(), secondMember.getGroup());
		assertEquals(firstMember.getState(), secondMember.getState());

		assertEquals(firstMember.getId(), entity.getId());
		assertEquals(firstMember.getUsername(), entity.getUsername());
		assertEquals(firstMember.getName(), entity.getName());
		assertEquals(firstMember.getGender(), entity.getGender());
		assertEquals(firstMember.getEmail(), entity.getEmail());
		assertEquals(firstMember.getGroup(), entity.getGroup());
		assertEquals(firstMember.getState(), entity.getState());
	}
	
	@Test
	void 회원_로그인_테스트() throws Exception {
		
		MemberEntity entity = repository.save(MemberEntity.builder()
				.username("loginMemberTester")
				.password(encoder.encode("password"))
				.name("name")
				.gender(Gender.MAIL)
				.email("test@example.com")
				.group(Group.ADMIN)
				.build());

		LoginMemberRequest request = LoginMemberRequest.builder()
				.username("loginMemberTester")
				.password("password")
				.build();

		MvcResult result = mockMvc.perform(post("/apis/members/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andReturn();

		MemberDetailsResponse member = objectMapper.readValue(
				result.getResponse().getContentAsString(), MemberDetailsResponse.class);

		verify(service, times(1)).loginMember(any());

		assertEquals(member.getId(), entity.getId());
		assertEquals(member.getUsername(), entity.getUsername());
		assertEquals(member.getName(), entity.getName());
		assertEquals(member.getGender(), entity.getGender());
		assertEquals(member.getEmail(), entity.getEmail());
		assertEquals(member.getGroup(), entity.getGroup());
		assertEquals(member.getState(), entity.getState());
	}

}
