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
import com.jong.msa.board.core.web.annotation.APIErrorResponses;
import com.jong.msa.board.core.web.dto.ErrorDetails;
import com.jong.msa.board.core.web.response.ErrorResponse;

import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;

@Configuration
public class SwaggerConfig {

	private List<APIErrorResponse> getErrorCodeResponseAnnotation(HandlerMethod handlerMethod) {

        Class<?> targetClass = handlerMethod.getBeanType();
        Class<?>[] interfaceClasses = targetClass.getInterfaces();

        Method targetMethod = handlerMethod.getMethod();

        List<APIErrorResponses> annotationList = new ArrayList<>();
        
        annotationList.addAll(Arrays.asList(targetClass.getAnnotationsByType(APIErrorResponses.class)));
        annotationList.addAll(Arrays.asList(targetMethod.getAnnotationsByType(APIErrorResponses.class)));
        
        for (Class<?> interfaceClass : interfaceClasses) {
			
        	try {

        		Method interfaceMethod = interfaceClass.getMethod(targetMethod.getName(), targetMethod.getParameterTypes());
			
                annotationList.addAll(Arrays.asList(interfaceClass.getAnnotationsByType(APIErrorResponses.class)));
        		annotationList.addAll(Arrays.asList(interfaceMethod.getAnnotationsByType(APIErrorResponses.class)));
        			
			} catch (NoSuchMethodException e) {
				
				throw new RuntimeException(e);
			}
		}
        
        return annotationList.stream()
        		.flatMap(x -> Arrays.stream(x.value()))
        		.collect(Collectors.toList());
	} 
	
	@Bean
	OperationCustomizer swaggerCustomizer(ObjectMapper objectMapper) {

	    return (Operation operation, HandlerMethod handlerMethod) -> {
	        
	        List<APIErrorResponse> annotationList = getErrorCodeResponseAnnotation(handlerMethod);

	        Map<HttpStatus, List<APIErrorResponse>> errorCodeResponseMap = annotationList.stream()
	        		.collect(Collectors.groupingBy(APIErrorResponse::status));

        	io.swagger.v3.oas.models.responses.ApiResponses apiResponses = operation.getResponses();

	        errorCodeResponseMap.entrySet().stream().forEach(apiErrorResponseEntry -> {

	        	io.swagger.v3.oas.models.responses.ApiResponse apiResponse = new io.swagger.v3.oas.models.responses.ApiResponse();
	        	io.swagger.v3.oas.models.media.Content content = new io.swagger.v3.oas.models.media.Content();
	        	io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType();

	        	HttpStatus status = apiErrorResponseEntry.getKey();

	        	List<APIErrorResponse> errorCodeResponses = apiErrorResponseEntry.getValue();
	        	
	        	errorCodeResponses.forEach(errorCodeResponse -> {
	        		
    				ErrorCode errorCode = errorCodeResponse.errorCode();

    				boolean useErrorDetailsList = errorCodeResponse.useErrorDetailsList();
    	        	
	        		try {

	        			io.swagger.v3.oas.models.examples.Example example = new io.swagger.v3.oas.models.examples.Example()
	        					.value(objectMapper.writeValueAsString(ErrorResponse.builder()
        		        				.errorCode(errorCode.getCode())
        		        				.errorMessage(errorCode.getMessage())
        		        				.errorDetailsList(useErrorDetailsList
        		        						? Arrays.asList(ErrorDetails.builder()
        		        								.field("field")
        		        								.message("message")
        		        								.build())
        		        						: new ArrayList<>())
        		        				.build()));
	        			
        				mediaType.addExamples(errorCode.getCode(), example);
	        		
					} catch (JsonProcessingException e) {
						
						throw new RuntimeException(e);
					}
        			
		        	apiResponses.addApiResponse(String.valueOf(status.value()), 
		        			apiResponse.content(content.addMediaType(MediaType.APPLICATION_JSON_VALUE, mediaType)));
	        	});
	        });

	        return operation;
	    };
	}
	
}
