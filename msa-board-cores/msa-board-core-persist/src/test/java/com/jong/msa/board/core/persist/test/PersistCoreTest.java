package com.jong.msa.board.core.persist.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.jong.msa.board.common.enums.Gender;
import com.jong.msa.board.common.enums.Group;
import com.jong.msa.board.common.enums.State;
import com.jong.msa.board.core.persist.test.entity.PersistCoreTestEntity;
import com.jong.msa.board.core.persist.test.repository.PersistCoreTestRepository;

@DataJpaTest
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class, 
		classes = PersistCoreTestContext.class)
public class PersistCoreTest {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	PersistCoreTestRepository repository;

	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@Test
	void 리포지토리_생성_확인() {

		assertNotNull(applicationContext.getBean(PersistCoreTestRepository.class));
	}
	
	@Test
	void 엔티티_필드_컨버팅_테스트() {

		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		UUID id = UUID.randomUUID();
		
		try {
			
			tx.begin();
			
			Query query = em.createNativeQuery(new StringBuilder()
					.append("INSERT INTO `tb_test`(")
						.append("  `id`")
						.append(", `test_gender`")
						.append(", `test_group`")
						.append(", `created_date_time`")
						.append(", `updated_date_time`")
						.append(", `state`")
					.append(") VALUES (")
						.append("  :id")
						.append(", :gender")
						.append(", :group")
						.append(", :createdDateTime")
						.append(", :updatedDateTime")
						.append(", :state")
					.append(")")
					.toString());
			
			query.setParameter("id", id)
				 .setParameter("gender", Gender.MAIL.getCode())
				 .setParameter("group", Group.ADMIN.getCode())
				 .setParameter("createdDateTime", LocalDateTime.now())
				 .setParameter("updatedDateTime", LocalDateTime.now())
				 .setParameter("state", State.ACTIVE.getCode());
		
			query.executeUpdate();
			
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}

		PersistCoreTestEntity entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
		
		assertEquals(Gender.MAIL, entity.getGender());
		assertEquals(Group.ADMIN, entity.getGroup());
		assertEquals(State.ACTIVE, entity.getState());
	}
	
	@Test
	void auditable_필드_테스트() throws InterruptedException {
		
		PersistCoreTestEntity entity = repository.save(PersistCoreTestEntity.builder()
				.gender(Gender.MAIL)
				.group(Group.ADMIN)
				.build());
		
		Thread.sleep(1000);
		
		repository.save(repository.findById(entity.getId())
				.orElseThrow(EntityNotFoundException::new)
				.setAuditable(false)
				.setGender(Gender.FEMAIL));

		// @PreUpdate 실행 
		repository.flush();
		
		assertEquals(entity.getCreatedDateTime(), entity.getUpdatedDateTime());
	}

}
