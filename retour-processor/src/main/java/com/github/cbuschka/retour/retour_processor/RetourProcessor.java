package com.github.cbuschka.retour.retour_processor;

public class RetourProcessor
{
	private static Logger logger = Logger.get();

	private ProcessRetourMessageValidator processRetourMessageValidator = new ProcessRetourMessageValidator();

	private ChargeSellerService chargeSellerService = new ChargeSellerService();

	private RefundBuyerService refundBuyerService = new RefundBuyerService();

	public void processRetour(ProcessRetourMessage message)
	{
		logger.log("Processing " + message + "...");

		processRetourMessageValidator.failIfInvalid(message);

		chargeSellerService.chargeSeller(message.getRetourNo());

		refundBuyerService.refundBuyer(message.getRetourNo());

		logger.log(message + " processed.");
	}
}
