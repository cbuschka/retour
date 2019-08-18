package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ProcessRetourHandler implements RequestHandler<SqsEnvelope, Void>
{
	private Logger logger = Logger.get();

	private RetourProcessor retourProcessor = new RetourProcessor();

	private ObjectMapper objectMapper = new ObjectMapper();

	public Void handleRequest(SqsEnvelope envelope, Context context)
	{
		if (envelope.Records.size() != 1)
		{
			throw new IllegalArgumentException("Expected single record, but was " + envelope.Records.size() + ".");
		}

		SqsEnvelope.Record firstRecord = envelope.Records.get(0);
		ProcessRetourMessage message = extractMessage(firstRecord);
		logger.run(() -> {
			this.retourProcessor.processRetour(message);
		}, context.getLogger());


		return null;
	}

	private ProcessRetourMessage extractMessage(SqsEnvelope.Record firstRecord)
	{
		try
		{
			return this.objectMapper.readValue(firstRecord.body, ProcessRetourMessage.class);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
