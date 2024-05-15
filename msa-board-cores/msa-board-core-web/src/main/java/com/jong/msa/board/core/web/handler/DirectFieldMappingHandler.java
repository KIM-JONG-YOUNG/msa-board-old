package com.jong.msa.board.core.web.handler;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DirectFieldMappingHandler {

	@InitBinder
	public void initBind(WebDataBinder dataBinder) {
		
		dataBinder.initDirectFieldAccess();	
	}
	
}
