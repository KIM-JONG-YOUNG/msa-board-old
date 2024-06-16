package com.jong.msa.board.domain.member.test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import com.jong.msa.board.common.enums.Gender;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.domain.member.entity.MemberEntity;
import com.jong.msa.board.domain.member.repository.MemberRepository;

@DataJpaTest
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class, 
		classes = MemberDomainTestContext.class)
public class MemberDomainTest {

	@Autowired
	MemberRepository repository;
	
	@Test
	void contextLoad() {
		
		System.out.println("context load.");
	}
	
	@Test
	void 회원계정_Unique_컬럼_테스트() {
		
		repository.save(MemberEntity.builder()
				.username("username")
				.password("password")
				.name("name")
				.gender(Gender.MAIL)
				.email("email")
				.group(Group.ADMIN)
				.build());
		
		repository.save(MemberEntity.builder()
				.username("username")
				.password("password")
				.name("name")
				.gender(Gender.MAIL)
				.email("email")
				.group(Group.ADMIN)
				.build());

		assertThrows(DataIntegrityViolationException.class, repository::flush);
	}
	
}
