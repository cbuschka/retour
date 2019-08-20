package com.github.cbuschka.retour.domain.refund_buyer;

import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import com.github.codestickers.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefundBuyerService
{
	private static Logger logger = LoggerFactory.getLogger(RefundBuyerService.class);

	@VisibleForTesting
	static final String QUEUE_NAME = "refund_buyer.fifo";

	@Autowired
	private SqsJsonMessageSender<RefundBuyerMessage> sqsJsonMessageSender;

	public void refundBuyer(String retourNo)
	{
		RefundBuyerMessage message = new RefundBuyerMessage(retourNo);

		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, message, retourNo);

		logger.info("Refund buyer message messageId=" + messageId + " sent.");
	}
}
