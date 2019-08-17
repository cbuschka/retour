package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SqsJsonMessageSender<T> {

	private ObjectMapper objectMapper = new ObjectMapper();

	private AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

	public void send(String queueName, T message) {

		String json = convertToJson(message);

		String queueUrl = locateQueue(queueName);

		sqs.sendMessage(queueUrl, json);
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
