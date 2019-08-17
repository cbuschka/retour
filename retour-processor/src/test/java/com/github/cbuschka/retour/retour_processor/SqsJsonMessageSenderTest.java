package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

public class SqsJsonMessageSenderTest {

	private static final String MESSAGE_AS_JSON = "json";
	private static final String A_QUEUE_URL = "queueUrl";
	private static final String QUEUE_NAME = "queueName";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@InjectMocks
	private SqsJsonMessageSender sqsJsonMessageSender;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private AmazonSQS amazonSQS;
	@Mock
	private Object messageObject;

	@Test
	public void sendsMessageAsJsonViaSqsClient() throws JsonProcessingException {
		givenIsALocatableQueue();
		givenIsAMessageConvertibleToJson();

		whenMessageIsSent();

		thenMessageObjectIsSentAsJson();
	}

	private void givenIsALocatableQueue() {
		GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
		when(this.amazonSQS.getQueueUrl(QUEUE_NAME)).thenReturn(getQueueUrlResult);
		when(getQueueUrlResult.getQueueUrl()).thenReturn(A_QUEUE_URL);
	}

	private void givenIsAMessageConvertibleToJson() throws JsonProcessingException {
		when(this.objectMapper.writeValueAsString(this.messageObject)).thenReturn(MESSAGE_AS_JSON);
	}

	private void thenMessageObjectIsSentAsJson() {
		verify(this.amazonSQS).sendMessage(A_QUEUE_URL, MESSAGE_AS_JSON);
	}

	private void whenMessageIsSent() {
		this.sqsJsonMessageSender.send(QUEUE_NAME, messageObject);
	}


}