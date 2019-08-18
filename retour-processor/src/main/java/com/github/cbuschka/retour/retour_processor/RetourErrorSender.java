package com.github.cbuschka.retour.retour_processor;

import com.github.cbuschka.retour.sqs.SqsJsonMessageSender;
import com.github.cbuschka.retour.util.Logger;
import com.github.codestickers.VisibleForTesting;

public class RetourErrorSender
{
	private static Logger logger = Logger.get();

	@VisibleForTesting
	static final String QUEUE_NAME = "retour_err";

	private SqsJsonMessageSender<RetourErrorMessage> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void sendError(String retourNo, String message)
	{
		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, new RetourErrorMessage(retourNo, message), null);

		logger.log("Error message messageId=" + messageId + " sent.");
	}
}
