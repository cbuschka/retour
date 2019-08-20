package com.github.cbuschka.retour.business.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.github.cbuschka.retour.AwsConfig;
import com.github.cbuschka.retour.RetourProcessorConfig;
import com.github.cbuschka.retour.infrastructure.lambda.SqsAwareLambdaHandlerAdapter;
import com.github.cbuschka.retour.infrastructure.spring.ApplicationContextFactory;
import com.github.codestickers.Used;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RetourLambdaHandler implements RequestStreamHandler
{
	private AnnotationConfigApplicationContext applicationContext;

	@Autowired
	private RetourProcessor retourProcessor;

	private SqsAwareLambdaHandlerAdapter<ReceiveRetourCommand> sqsAwareLambdaHandlerAdapter = new SqsAwareLambdaHandlerAdapter<>(ReceiveRetourCommand.class);

	@Used("Required for aws.")
	public RetourLambdaHandler()
	{
		this(ApplicationContextFactory.newApplicationContext(RetourProcessorConfig.class, AwsConfig.class));
	}

	public RetourLambdaHandler(AnnotationConfigApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
		this.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
	}

	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException
	{
		this.sqsAwareLambdaHandlerAdapter.handleRequest(input, output, context,
				(m, c) -> this.retourProcessor.processRetour(m));
	}

	void destroy()
	{
		this.applicationContext.close();
	}
}
