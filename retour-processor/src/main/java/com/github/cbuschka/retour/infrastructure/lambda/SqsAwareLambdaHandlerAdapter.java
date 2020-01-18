package com.github.cbuschka.retour.infrastructure.lambda;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SqsAwareLambdaHandlerAdapter implements LambdaHandlerAdapter {

	private ObjectMapper objectMapper = new ObjectMapper();

	public boolean handles(JsonNode jsonNode) {
		return jsonNode.isObject()
				&& jsonNode.has("Records")
				&& jsonNode.get("Records").isArray()
				&& jsonNode.get("Records").size() == 1
				&& jsonNode.get("Records").get(0).has("body")
				&& jsonNode.get("Records").get(0).get("body").isTextual();
	}

	@Override
	public <T> T extractRequest(JsonNode jsonNode, Class<T> inputType) throws IOException {
		return objectMapper.readerFor(inputType).readValue(jsonNode.get("Records").get(0).get("body").asText());
	}
}
