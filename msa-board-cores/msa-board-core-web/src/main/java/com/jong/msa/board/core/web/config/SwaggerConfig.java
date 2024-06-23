package com.jong.msa.board.core.web.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jong.msa.board.common.enums.ErrorCode;
import com.jong.msa.board.core.web.annotation.APIErrorResponse;
import com.jong.msa.board.core.web.dto.ErrorDetails;
import com.jong.msa.board.core.web.response.ErrorResponse;

import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

	private final ObjectMapper objectMapper;
	
	private List<APIErrorResponse> getErrorCodeResponseAnnotation(HandlerMethod handlerMethod) {
	
        Class<?>[] interfaces = handlerMethod.getBeanType().getInterfaces();
        Method method = handlerMethod.getMethod();
        
        List<APIErrorResponse> annotationList = new ArrayList<>();
        
        annotationList.addAll(Arrays.asList(method.getAnnotationsByType(APIErrorResponse.class)));
        
        for (Class<?> clazz : interfaces) {
			
        	try {

        		Method interfaceMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
			
        		annotationList.addAll(Arrays.asList(interfaceMethod.getAnnotationsByType(APIErrorResponse.class)));
        			
			} catch (NoSuchMethodException e) {
				
				throw new RuntimeException(e);
			}
		}
        
        return annotationList;
	} 
	
	@Bean
	OperationCustomizer swaggerCustomizer() {

	    return (Operation operation, HandlerMethod handlerMethod) -> {
	        
	        List<APIErrorResponse> annotationList = getErrorCodeResponseAnnotation(handlerMethod);

	        Map<HttpStatus, List<APIErrorResponse>> errorResponseMap = annotationList.stream()
	        		.collect(Collectors.groupingBy(APIErrorResponse::status));
//	        		.entrySet().stream()
//	        		.collect(Collectors.toMap(
//	        				x -> x.getKey(), 
//	        				x -> x.getValue().stream()
//	        					.map(APIErrorResponse::errorCode)
//	        					.collect(Collectors.toCollection(LinkedHashSet::new))));

	        errorResponseMap.entrySet().stream().forEach(errorResponseEntry -> {

	        	io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();
	        	io.swagger.v3.oas.models.media.Content content = new io.swagger.v3.oas.models.media.Content();
	        	io.swagger.v3.oas.models.responses.ApiResponse apiResponse = new io.swagger.v3.oas.models.responses.ApiResponse();

	        	errorResponseEntry.getValue().forEach(errorResponse -> {
	        		
	        		try {

	        			ErrorCode errorCode = errorResponse.errorCode();
	        			
	        			io.swagger.v3.oas.models.examples.Example example = new io.swagger.v3.oas.models.examples.Example();

		        		example.setValue(objectMapper.writeValueAsString(ErrorResponse.builder()
		        				.errorCode(errorCode.getCode())
		        				.errorMessage(errorCode.getMessage())
		        				.errorDetailsList(errorResponse.useErrorDetailsList()
		        						? Arrays.asList(ErrorDetails.builder()
		        								.field("field")
		        								.message("message")
		        								.build())
		        						: new ArrayList<>())
		        				.build()));

		        		mediaType.addExamples(errorCode.getCode(), example);
			        	
		        		content.addMediaType(MediaType.APPLICATION_JSON_VALUE, mediaType);
			        	
			        	apiResponse.setDescription(errorResponseEntry.getKey().name());
			        	apiResponse.setContent(content);
			        	
			            operation.getResponses().addApiResponse(String.valueOf(errorResponseEntry.getKey().value()), apiResponse);
			            
					} catch (JsonProcessingException e) {
						
						throw new RuntimeException(e);
					}
	        	});
	        });

	        return operation;
	    };
	}
	
}
