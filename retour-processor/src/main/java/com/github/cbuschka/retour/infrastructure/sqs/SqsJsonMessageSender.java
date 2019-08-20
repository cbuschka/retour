package com.github.cbuschka.retour.infrastructure.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SqsJsonMessageSender<T>
{
	private static final String DUMMY_MESSAGE_GROUP_ID = "default";

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private AmazonSQS sqs;

	public String send(String queueName, T message, String dedupId)
	{
		String json = convertToJson(message);

		String queueUrl = locateQueue(queueName);

		SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, json);
		if (dedupId != null)
		{
			sendMessageRequest.setMessageGroupId(DUMMY_MESSAGE_GROUP_ID);
			sendMessageRequest.setMessageDeduplicationId(dedupId);
		}
		SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
		return sendMessageResult.getMessageId();
	}

	private String locateQueue(String queueName)
	{
		GetQueueUrlResult getQueueUrlResult = sqs.getQueueUrl(queueName);
		return getQueueUrlResult.getQueueUrl();
	}

	private String convertToJson(T message)
	{
		try
		{
			return objectMapper.writeValueAsString(message);
		}
		catch (IOException ex)
		{
			throw new RuntimeException("Serializing to json failed.", ex);
		}
	}

}
