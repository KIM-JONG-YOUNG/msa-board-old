package com.jong.msa.board.core.security.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.jong.msa.board.common.enums.DBCodeEnum.Group;
import com.jong.msa.board.core.security.test.annotation.SecurityCoreTestUser;
import com.jong.msa.board.core.security.utils.SecurityContextUtils;

@SpringBootTest
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class,
		classes = SecurityCoreTestContext.class)
public class SecurityCoreTest {

	public static final String TEST_ID = "f77142d4-e247-4ac2-b466-7c19049c504f";
	
	@Test
	@SecurityCoreTestUser(id = TEST_ID, groups = Group.ADMIN)
	void SecurityContextUtils_테스트() {
		
		System.out.println(SecurityContextUtils.getAuthenticationId());
		assertEquals(TEST_ID, SecurityContextUtils.getAuthenticationId().toString());
	}

}
