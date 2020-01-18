package com.github.cbuschka.retour.infrastructure.lambda;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class RawLambdaHandlerAdapter implements LambdaHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(RawLambdaHandlerAdapter.class);

	@Autowired
	private ObjectMapper objectMapper;

	public boolean handles(JsonNode json) {
		return true;
	}

	@Override
	public <T> T extractRequest(JsonNode json, Class<T> inputType) throws IOException {
		return objectMapper.readerFor(inputType).readValue(json);
	}
}
