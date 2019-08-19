package com.github.cbuschka.retour.infrastructure.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.codestickers.Unused;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;

public class SqsAwareLambdaHandlerAdapter<T>
{
	private ObjectMapper objectMapper = new ObjectMapper();

	private Class<T> inputType;

	public SqsAwareLambdaHandlerAdapter(Class<T> inputType)
	{
		this.inputType = inputType;
	}

	@SuppressWarnings("unused")
	public final void handleRequest(InputStream input, @Unused("no response support") OutputStream output,
									Context context, BiConsumer<T, Context> requestHandler) throws IOException
	{
		T event = readRequest(input);
		requestHandler.accept(event, context);
	}

	private T readRequest(InputStream input) throws IOException
	{
		JsonNode jsonNode = objectMapper.readTree(input);
		if (jsonNode.isObject()
				&& jsonNode.has("Records")
				&& jsonNode.get("Records").isArray()
				&& jsonNode.get("Records").size() == 1
				&& jsonNode.get("Records").get(0).has("body")
				&& jsonNode.get("Records").get(0).get("body").isTextual())
		{
			return objectMapper.readerFor(this.inputType).readValue(jsonNode.get("Records").get(0).get("body").asText());
		}
		else
		{
			return objectMapper.readerFor(this.inputType).readValue(jsonNode);
		}
	}
}
