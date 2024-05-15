package com.jong.msa.board.core.validation.test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import com.jong.msa.board.core.validation.test.dto.ValidationCoreTestDTO;
import com.jong.msa.board.core.validation.test.dto.ValidationCoreTestDTO2;
import com.jong.msa.board.core.validation.utils.BindingResultUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ImportAutoConfiguration({ ValidationAutoConfiguration.class })
@ContextConfiguration(
		initializers = ConfigDataApplicationContextInitializer.class,
		classes = ValidationCoreTestContext.class)
public class ValidationCoreTest {

	@Autowired
	Validator validator;
	
	String getFieldErrorMessage(BindingResult bindingResult, String field) {
		
		return bindingResult.getFieldErrors().stream()
				.filter(error -> error.getField().equals(field))
				.findAny().orElseThrow(RuntimeException::new)
				.getDefaultMessage();
	}
	
	@Test
	void StringValidation_어노테이션_테스트() {
		
		ValidationCoreTestDTO dto = ValidationCoreTestDTO.builder()
				.blankCheckField(null)
				.nullableBlankCheckField(null)
				.sizeCheckField("123456789012")
				.patternCheckField("한글")
				.build();
		
		BindingResult bindingResult = BindingResultUtils.validate(dto, validator);
		
		List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
		
		fieldErrorList.stream().forEach(x -> log.info("{} : {}", x.getField(), x.getDefaultMessage()));
		
		assertTrue(fieldErrorList.stream().filter(x -> x.getField().equals("blankCheckField")).count() == 1);
		assertFalse(fieldErrorList.stream().filter(x -> x.getField().equals("nullableBlankCheckField")).count() == 1);
		assertTrue(fieldErrorList.stream().filter(x -> x.getField().equals("sizeCheckField")).count() == 1);
		assertTrue(fieldErrorList.stream().filter(x -> x.getField().equals("patternCheckField")).count() == 1);
	}
	
	@Test
	void BetweenDate_어노테이션_테스트() {
		
		ValidationCoreTestDTO2 dto = ValidationCoreTestDTO2.builder()
				.fromTime(LocalTime.now())
				.toTime(LocalTime.now().minusSeconds(1))
				.fromDate(LocalDate.now())
				.toDate(LocalDate.now().minusDays(1))
				.fromDateTime(LocalDateTime.now())
				.toDateTime(LocalDateTime.now().minusYears(1))
				.build();
		
		BindingResult bindingResult = BindingResultUtils.validate(dto, validator);

		List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
		
		fieldErrorList.stream().forEach(x -> log.info("{} : {}", x.getField(), x.getDefaultMessage()));

		assertTrue(fieldErrorList.stream().filter(x -> x.getField().equals("fromTime")).count() == 1);
		assertTrue(fieldErrorList.stream().filter(x -> x.getField().equals("fromDate")).count() == 1);
		assertTrue(fieldErrorList.stream().filter(x -> x.getField().equals("fromDateTime")).count() == 1);
	}	
	
}
