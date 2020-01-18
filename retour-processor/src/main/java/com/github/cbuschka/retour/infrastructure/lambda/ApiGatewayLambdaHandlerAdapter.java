package com.github.cbuschka.retour.infrastructure.lambda;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ApiGatewayLambdaHandlerAdapter implements LambdaHandlerAdapter {

	@Autowired
	private ObjectMapper objectMapper;

	public boolean handles(JsonNode jsonNode) {
		return jsonNode.isObject()
				&& jsonNode.has("resource")
				&& jsonNode.get("resource").isTextual()
				&& jsonNode.has("requestContext")
				&& jsonNode.get("requestContext").isObject()
				&& jsonNode.get("body").isTextual();
	}

	@Override
	public <T> T extractRequest(JsonNode jsonNode, Class<T> inputType) throws IOException {
		String bodyJson = jsonNode.get("body").asText();
		return objectMapper.readerFor(inputType).readValue(bodyJson);
	}
}
