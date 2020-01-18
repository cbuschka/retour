package com.github.cbuschka.retour;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cbuschka.retour.infrastructure.lambda.ApiGatewayLambdaHandlerAdapter;
import com.github.cbuschka.retour.infrastructure.lambda.RawLambdaHandlerAdapter;
import com.github.cbuschka.retour.infrastructure.lambda.SqsAwareLambdaHandlerAdapter;
import com.github.cbuschka.retour.infrastructure.lambda.UniversalLambdaHandlerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@ComponentScan(basePackages = "com.github.cbuschka.retour")
@Configuration
public class RetourProcessorConfig {
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public UniversalLambdaHandlerAdapter lambdaHandlerAdapter() {
		return new UniversalLambdaHandlerAdapter(Arrays.asList(sqsAwareLambdaHandlerAdapter(),
				apiGatewayLambdaHandlerAdapter(),
				rawLambdaHandlerAdapter()));
	}

	@Bean
	protected SqsAwareLambdaHandlerAdapter sqsAwareLambdaHandlerAdapter() {
		return new SqsAwareLambdaHandlerAdapter();
	}

	@Bean
	protected ApiGatewayLambdaHandlerAdapter apiGatewayLambdaHandlerAdapter() {
		return new ApiGatewayLambdaHandlerAdapter();
	}

	@Bean
	protected RawLambdaHandlerAdapter rawLambdaHandlerAdapter() {
		return new RawLambdaHandlerAdapter();
	}
}
