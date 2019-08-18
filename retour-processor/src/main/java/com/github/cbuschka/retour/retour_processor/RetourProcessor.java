package com.github.cbuschka.retour.retour_processor;

public class RetourProcessor
{
	private static Logger logger = Logger.get();

	private RetourValidator retourValidator = new RetourValidator();

	private ChargeSellerService chargeSellerService = new ChargeSellerService();

	private RefundBuyerService refundBuyerService = new RefundBuyerService();

	private RetourErrorSender retourErrorSender = new RetourErrorSender();

	public void processRetour(RetourMessage message)
	{
		logger.log("Processing " + message + "...");

		RetourValidationResult validationResult = retourValidator.getViolations(message);
		if (!validationResult.isValid())
		{
			logger.log(message + " is invalid: " + validationResult.toMessage());

			retourErrorSender.sendError(message.getRetourNo(), validationResult.toMessage());
		}
		else
		{
			chargeSellerService.chargeSeller(message.getRetourNo());

			refundBuyerService.refundBuyer(message.getRetourNo());
		}

		logger.log(message + " processed.");
	}
}
