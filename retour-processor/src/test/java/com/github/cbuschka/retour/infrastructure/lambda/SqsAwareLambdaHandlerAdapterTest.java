package com.github.cbuschka.retour.infrastructure.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SqsAwareLambdaHandlerAdapterTest
{
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private Context context;

	private SqsAwareLambdaHandlerAdapter<HashMap> adapter = new SqsAwareLambdaHandlerAdapter<>(HashMap.class);

	private String inputJson;
	private HashMap inputRequest;

	@Test
	public void invokesWithConcreteType() throws IOException
	{
		givenIsInput("{\"key\":\"value\"}");

		whenRequestHandlerCalled();

		thenRequestIsValid();
	}


	@Test
	public void unwrapsFromSqsEnvelope() throws IOException
	{
		givenIsInput("{\"Records\":[{\"body\":\"{\\\"key\\\":\\\"value\\\"}\"}]}");

		whenRequestHandlerCalled();

		thenRequestIsValid();
	}

	private void thenRequestIsValid()
	{
		Map<String, Object> expected = new HashMap<>();
		expected.put("key", "value");
		assertThat(this.inputRequest, is(expected));
	}

	private void whenRequestHandlerCalled() throws IOException
	{
		this.adapter.handleRequest(new ByteArrayInputStream(this.inputJson.getBytes(StandardCharsets.UTF_8)),
				new ByteArrayOutputStream(),
				context, (m, c) -> this.inputRequest = m);
	}

	private void givenIsInput(String json)
	{
		this.inputJson = json;
	}
}
