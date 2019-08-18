package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import com.github.cbuschka.retour.util.Logger;
import com.github.codestickers.VisibleForTesting;

import java.util.Collections;
import java.util.Map;

public class RetourAckSender
{
	private static Logger logger = Logger.get();

	@VisibleForTesting
	static final String QUEUE_NAME = "retour_ack.fifo";

	private SqsJsonMessageSender<Map> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void sendAck(String retourNo)
	{
		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, Collections.emptyMap(), retourNo);

		logger.log("Ack message messageId=" + messageId + " sent.");
	}
}
