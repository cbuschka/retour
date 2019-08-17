package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ProcessRetourHandler implements RequestHandler<ProcessRetourMessage, Void> {

	private ProcessRetourMessageValidator processRetourMessageValidator = new ProcessRetourMessageValidator();

	private ChargeSellerService chargeSellerService = new ChargeSellerService();

	private RefundBuyerService refundBuyerService = new RefundBuyerService();

	public Void handleRequest(ProcessRetourMessage message, Context context) {
		context.getLogger().log(String.valueOf(message));

		processRetourMessageValidator.failIfInvalid(message);

		chargeSellerService.chargeSeller(message.getRetourNo());

		refundBuyerService.refundBuyer(message.getRetourNo());

		return null;
	}
}
