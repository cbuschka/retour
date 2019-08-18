package com.github.cbuschka.retour.domain.charge_seller;

import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import com.github.cbuschka.retour.util.Logger;
import com.github.codestickers.VisibleForTesting;

public class ChargeSellerService
{
	private static Logger logger = Logger.get();

	@VisibleForTesting
	static final String QUEUE_NAME = "charge_seller.fifo";

	private SqsJsonMessageSender<ChargeSellerMessage> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void chargeSeller(String retourNo)
	{
		ChargeSellerMessage message = new ChargeSellerMessage(retourNo);

		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, message, retourNo);

		logger.log("Charge seller message messageId=" + messageId + " sent.");
	}
}
