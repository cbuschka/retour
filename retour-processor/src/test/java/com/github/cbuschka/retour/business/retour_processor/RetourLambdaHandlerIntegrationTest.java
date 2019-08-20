package com.github.cbuschka.retour.business.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.github.cbuschka.retour.AwsConfig;
import com.github.cbuschka.retour.RetourProcessorConfig;
import com.github.cbuschka.retour.infrastructure.spring.ApplicationContextFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RetourLambdaHandlerIntegrationTest
{
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private Context context;

	private AnnotationConfigApplicationContext applicationContext;

	private RetourLambdaHandler retourLambdaHandler;

	@Before
	public void before()
	{
		this.applicationContext = ApplicationContextFactory.newApplicationContext(RetourProcessorConfig.class, AwsConfig.class);
		this.retourLambdaHandler = new RetourLambdaHandler(this.applicationContext);
	}

	@Test
	public void handlesInvalidMessage() throws IOException
	{
		this.retourLambdaHandler.handleRequest(new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)),
				new ByteArrayOutputStream(),
				context);
	}

	@After
	public void after()
	{
		this.retourLambdaHandler.destroy();
	}
}
