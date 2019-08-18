package com.github.cbuschka.retour.retour_processor;

import com.github.codestickers.VisibleForTesting;

import java.util.Collections;
import java.util.Map;

public class RefundBuyerService {

	private static Logger logger = Logger.get();

	@VisibleForTesting
	static final String QUEUE_NAME = "refund_buyer.fifo";

	private SqsJsonMessageSender<Map> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void refundBuyer(String retourNo) {
		sqsJsonMessageSender.send(QUEUE_NAME, Collections.emptyMap(), retourNo);

		logger.log("Refund buyer message sent.");
	}
}
