package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class ProcessRetourHandler implements RequestHandler<Map<String, Object>, Void>
{
	private ChargeSellerService chargeSellerService = new ChargeSellerService();

	public Void handleRequest(Map<String, Object> event, Context context)
	{
		String message = String.valueOf(event);
		context.getLogger().log(message);

		chargeSellerService.chargeSeller();

		return null;
	}
}
