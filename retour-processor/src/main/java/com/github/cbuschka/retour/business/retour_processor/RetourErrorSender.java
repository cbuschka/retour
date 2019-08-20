package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetourErrorSender
{
	private static Logger logger = LoggerFactory.getLogger(RetourErrorSender.class);

	private static final String QUEUE_NAME = "retour_err";

	@Autowired
	private SqsJsonMessageSender<RetourErrorMessage> sqsJsonMessageSender;

	public void sendError(String retourNo, Exception ex)
	{
		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, new RetourErrorMessage(retourNo, ex.getMessage()), null);

		logger.info("Error message messageId=" + messageId + " sent.");
	}
}
