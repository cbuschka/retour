package com.github.cbuschka.retour.domain.refund_buyer;

import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import com.github.codestickers.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefundBuyerService
{
	private static Logger logger = LoggerFactory.getLogger(RefundBuyerService.class);

	@VisibleForTesting
	static final String QUEUE_NAME = "refund_buyer.fifo";

	private SqsJsonMessageSender<RefundBuyerMessage> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void refundBuyer(String retourNo)
	{
		RefundBuyerMessage message = new RefundBuyerMessage(retourNo);

		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, message, retourNo);

		logger.info("Refund buyer message messageId=" + messageId + " sent.");
	}
}
