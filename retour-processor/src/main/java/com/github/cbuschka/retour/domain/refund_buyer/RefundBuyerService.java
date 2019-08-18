package com.github.cbuschka.retour.domain.refund_buyer;

import com.github.cbuschka.retour.util.Logger;
import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import com.github.codestickers.VisibleForTesting;

public class RefundBuyerService
{

	private static Logger logger = Logger.get();

	@VisibleForTesting
	static final String QUEUE_NAME = "refund_buyer.fifo";

	private SqsJsonMessageSender<RefundBuyerMessage> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void refundBuyer(String retourNo)
	{
		RefundBuyerMessage message = new RefundBuyerMessage(retourNo);

		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, message, retourNo);

		logger.log("Refund buyer message messageId=" + messageId + " sent.");
	}
}
