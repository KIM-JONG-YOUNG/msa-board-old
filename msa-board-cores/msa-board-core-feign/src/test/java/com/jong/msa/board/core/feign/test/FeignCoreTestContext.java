package com.jong.msa.board.core.feign.test;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureWebMvc
@ImportAutoConfiguration(FeignAutoConfiguration.class)
@ComponentScan(basePackages = "com.jong.msa.board")
public class FeignCoreTestContext {

}
