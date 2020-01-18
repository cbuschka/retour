package com.github.cbuschka.retour.infrastructure.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.codestickers.Unused;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

public class UniversalLambdaHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(UniversalLambdaHandlerAdapter.class);

	@Autowired
	private ObjectMapper objectMapper;

	private List<LambdaHandlerAdapter> adapters;

	public UniversalLambdaHandlerAdapter(List<LambdaHandlerAdapter> adapters) {
		this.adapters = adapters;
	}

	@SuppressWarnings("unused")
	public final <T> void handleRequest(InputStream input, Class<T> inputType, @Unused("no response support") OutputStream output,
										Context context, BiConsumer<T, Context> requestHandler) {
		T event;
		try {
			JsonNode jsonNode = objectMapper.readTree(input);
			logger.info("Request is: {}", objectMapper.writer().writeValueAsString(jsonNode));

			LambdaHandlerAdapter adapter = pickAdapterFor(jsonNode);
			event = adapter.extractRequest(jsonNode, inputType);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		requestHandler.accept(event, context);
	}

	private LambdaHandlerAdapter pickAdapterFor(JsonNode jsonNode) {
		for (LambdaHandlerAdapter adapter : this.adapters) {
			if (adapter.handles(jsonNode)) {
				return adapter;
			}
		}

		throw new NoSuchElementException("No matching handler adapter found.");
	}
}
