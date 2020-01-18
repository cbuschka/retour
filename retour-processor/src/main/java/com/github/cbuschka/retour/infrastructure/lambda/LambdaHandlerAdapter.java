package com.github.cbuschka.retour.infrastructure.lambda;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface LambdaHandlerAdapter {
	boolean handles(JsonNode json);

	<T> T extractRequest(JsonNode json, Class<T> inputType) throws IOException;
}
