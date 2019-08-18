package com.github.cbuschka.retour.infrastructure.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class SqsAwareLambdaHandler<T> implements RequestHandler<SqsEnvelope, Void>
{
	private ObjectMapper objectMapper = new ObjectMapper();

	private Class<T> inputType;

	protected SqsAwareLambdaHandler(Class<T> inputType) {
		this.inputType = inputType;
	}

	public final Void handleRequest(SqsEnvelope envelope, Context context)
	{
		if (envelope.Records.size() != 1)
		{
			throw new IllegalArgumentException("Expected single record, but was " + envelope.Records.size() + ".");
		}

		SqsEnvelope.Record firstRecord = envelope.Records.get(0);
		T event = extractMessage(firstRecord);
		ThreadLocalContext.runWith(() -> {
			handle(event, context);
		}, context);

		return null;
	}

	protected abstract void handle(T t, Context context);

	private T extractMessage(SqsEnvelope.Record firstRecord)
	{
		try
		{
			return this.objectMapper.readValue(firstRecord.body, this.inputType);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
