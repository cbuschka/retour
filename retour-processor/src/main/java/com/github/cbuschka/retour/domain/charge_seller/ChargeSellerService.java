package com.github.cbuschka.retour.domain.charge_seller;

import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import com.github.codestickers.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChargeSellerService
{
	private static Logger logger = LoggerFactory.getLogger(ChargeSellerService.class);

	@VisibleForTesting
	static final String QUEUE_NAME = "charge_seller.fifo";

	private SqsJsonMessageSender<ChargeSellerMessage> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void chargeSeller(String retourNo)
	{
		ChargeSellerMessage message = new ChargeSellerMessage(retourNo);

		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, message, retourNo);

		logger.info("Charge seller message messageId=" + messageId + " sent.");
	}
}
