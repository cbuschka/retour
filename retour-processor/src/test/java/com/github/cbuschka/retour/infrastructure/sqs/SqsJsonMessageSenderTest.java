package com.github.cbuschka.retour.infrastructure.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SqsJsonMessageSenderTest
{

	private static final String MESSAGE_AS_JSON = "json";
	private static final String A_QUEUE_URL = "queueUrl";
	private static final String QUEUE_NAME = "queueName";
	private static final String MESSAGE_ID = "messageId";
	private static final String DEDUP_ID = "dedupId";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@InjectMocks
	private SqsJsonMessageSender<Object> sqsJsonMessageSender;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private AmazonSQS amazonSQS;
	@Mock
	private Object messageObject;
	@Mock
	private SendMessageResult sendMessageResult;

	private String messageId = "messageIdUnchanged";

	@Before
	public void before()
	{
		givenIsAMessageIdForMessageSent();
	}

	private void givenIsAMessageIdForMessageSent()
	{
		when(this.amazonSQS.sendMessage(Mockito.any(SendMessageRequest.class))).thenReturn(this.sendMessageResult);
		when(this.sendMessageResult.getMessageId()).thenReturn(MESSAGE_ID);
	}

	@Test
	public void sendsMessageAsJsonViaSqsClient() throws JsonProcessingException
	{
		givenIsALocatableQueue();
		givenIsAMessageConvertibleToJson();

		whenMessageIsSent();

		thenMessageObjectIsSentAsJson();
		thenMessageIdIsReturned();
	}

	private void thenMessageIdIsReturned()
	{
		assertThat(this.messageId, is(MESSAGE_ID));
	}

	private void givenIsALocatableQueue()
	{
		GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
		when(this.amazonSQS.getQueueUrl(QUEUE_NAME)).thenReturn(getQueueUrlResult);
		when(getQueueUrlResult.getQueueUrl()).thenReturn(A_QUEUE_URL);
	}

	private void givenIsAMessageConvertibleToJson() throws JsonProcessingException
	{
		when(this.objectMapper.writeValueAsString(this.messageObject)).thenReturn(MESSAGE_AS_JSON);
	}

	private void thenMessageObjectIsSentAsJson()
	{
		ArgumentCaptor<SendMessageRequest> capture = ArgumentCaptor.forClass(SendMessageRequest.class);
		verify(this.amazonSQS).sendMessage(capture.capture());
		SendMessageRequest request = capture.getValue();
		assertThat(request.getQueueUrl(), is(A_QUEUE_URL));
		assertThat(request.getMessageBody(), is(MESSAGE_AS_JSON));
		assertThat(request.getMessageDeduplicationId(), is(DEDUP_ID));
		assertThat(request.getMessageGroupId(), is(not(nullValue())));


	}

	private void whenMessageIsSent()
	{
		this.messageId = this.sqsJsonMessageSender.send(QUEUE_NAME, messageObject, DEDUP_ID);
	}


}
