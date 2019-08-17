package com.github.cbuschka.retour.retour_processor;

import com.github.codestickers.VisibleForTesting;

import java.util.Collections;
import java.util.Map;

public class RefundBuyerService {

	@VisibleForTesting
	static final String QUEUE_NAME = "refund_buyer.fifo";

	private SqsJsonMessageSender<Map> sqsJsonMessageSender = new SqsJsonMessageSender<>();

	public void refundBuyer() {
		sqsJsonMessageSender.send(QUEUE_NAME, Collections.emptyMap());
	}
}
