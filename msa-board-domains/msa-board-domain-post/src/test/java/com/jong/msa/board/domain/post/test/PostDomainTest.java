package com.jong.msa.board.domain.post.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;

import com.jong.msa.board.domain.post.entity.PostEntity;
import com.jong.msa.board.domain.post.repository.PostRepository;

@DataJpaTest
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class, 
		classes = PostDomainTestContext.class)
public class PostDomainTest {

	@Autowired
	PostRepository repository;
	
	@Test
	void contextLoad() {
		
		System.out.println("context load.");
	}
	
	@Test
	void PostEntity_조회수_저장_테스트() {
		
		PostEntity entity = repository.save(PostEntity.builder()
				.title("title")
				.content("content")
				.writerId(UUID.randomUUID())
				.views(-100)
				.build());

		repository.flush();
		
		assertTrue(repository.findById(entity.getId()).orElseThrow(EntityNotFoundException::new).getViews() >= 0);
	}
	
}
