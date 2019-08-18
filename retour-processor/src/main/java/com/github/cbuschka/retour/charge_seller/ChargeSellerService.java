package com.github.cbuschka.retour.charge_seller;

import com.github.cbuschka.retour.util.Logger;
import com.github.cbuschka.retour.sqs.SqsJsonMessageSender;
import com.github.codestickers.VisibleForTesting;

import java.util.Collections;
import java.util.Map;

public class ChargeSellerService
{
	private static Logger logger = Logger.get();

	@VisibleForTesting
	static final String QUEUE_NAME = "charge_seller.fifo";

	private SqsJsonMessageSender<Map> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void chargeSeller(String retourNo)
	{
		String messageId = sqsJsonMessageSender.send(QUEUE_NAME, Collections.emptyMap(), retourNo);

		logger.log("Charge seller message messageId=" + messageId + " sent.");
	}
}
