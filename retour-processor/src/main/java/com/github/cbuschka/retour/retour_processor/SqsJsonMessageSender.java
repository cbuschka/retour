package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SqsJsonMessageSender<T> {

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String DUMMY_MESSAGE_GROUP_ID = "default";

	private AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

	public void send(String queueName, T message, String dedupId) {

		String json = convertToJson(message);

		String queueUrl = locateQueue(queueName);

		SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, json);
		sendMessageRequest.setMessageGroupId(DUMMY_MESSAGE_GROUP_ID);
		sendMessageRequest.setMessageDeduplicationId(dedupId);
		sqs.sendMessage(sendMessageRequest);
	}

	private String locateQueue(String queueName) {
		GetQueueUrlResult getQueueUrlResult = sqs.getQueueUrl(queueName);
		return getQueueUrlResult.getQueueUrl();
	}

	private String convertToJson(T message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (IOException ex) {
			throw new RuntimeException("Serializing to json failed.", ex);
		}
	}

}
