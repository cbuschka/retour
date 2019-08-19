package com.github.cbuschka.retour.business.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.github.cbuschka.retour.infrastructure.lambda.SqsAwareLambdaHandlerAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RetourLambdaHandler implements RequestStreamHandler
{
	private RetourProcessor retourProcessor = new RetourProcessor();

	private SqsAwareLambdaHandlerAdapter<RetourMessage> sqsAwareLambdaHandlerAdapter = new SqsAwareLambdaHandlerAdapter<>(RetourMessage.class);

	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException
	{
		this.sqsAwareLambdaHandlerAdapter.handleRequest(input, output, context,
				(m, c) -> this.retourProcessor.processRetour(m));
	}
}
