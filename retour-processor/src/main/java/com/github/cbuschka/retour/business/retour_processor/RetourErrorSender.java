package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.domain.refund_buyer.RefundBuyerService;
import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetourErrorSender
{
	private static Logger logger = LoggerFactory.getLogger(RetourErrorSender.class);


	private static final String QUEUE_NAME = "retour_err";

	private SqsJsonMessageSender<RetourErrorMessage> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void sendError(String retourNo, Exception ex)
	{
		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, new RetourErrorMessage(retourNo, ex.getMessage()), null);

		logger.info("Error message messageId=" + messageId + " sent.");
	}
}
