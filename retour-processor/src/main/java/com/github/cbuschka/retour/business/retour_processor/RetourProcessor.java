package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.domain.charge_seller.ChargeSellerService;
import com.github.cbuschka.retour.domain.order_store.OrderDao;
import com.github.cbuschka.retour.domain.order_store.OrderNotFound;
import com.github.cbuschka.retour.domain.refund_buyer.RefundBuyerService;
import com.github.cbuschka.retour.domain.retour_store.RetourAlreadyProcessed;
import com.github.cbuschka.retour.domain.retour_store.RetourDao;
import com.github.cbuschka.retour.util.Logger;

public class RetourProcessor
{
	private static Logger logger = Logger.get();

	private RetourValidator retourValidator = new RetourValidator();

	private ChargeSellerService chargeSellerService = new ChargeSellerService();

	private RefundBuyerService refundBuyerService = new RefundBuyerService();

	private RetourErrorSender retourErrorSender = new RetourErrorSender();

	private RetourAckSender retourAckSender = new RetourAckSender();

	private OrderDao orderDao = new OrderDao();

	private RetourDao retourDao = new RetourDao();

	public void processRetour(RetourMessage message)
	{
		logger.log("Processing " + message + "...");

		try {
			retourValidator.validate(message);

			orderDao.findOrder(message.getOrderNo());

			String retourNo = message.getRetourNo();
			retourDao.createRetour(retourNo, message.getOrderNo());

			chargeSellerService.chargeSeller(retourNo);
			refundBuyerService.refundBuyer(retourNo);
			retourAckSender.sendAck(retourNo);

			retourDao.markRetourProcessed(retourNo);
		}
		catch (RetourMessageInvalid | RetourAlreadyProcessed | OrderNotFound ex)
		{
			sendErrorMessage(message.getRetourNo(), ex.getMessage());
		}

		logger.log(message + " processed.");
	}

	private void sendErrorMessage(String retourNo, String message)
	{
		logger.log(message);
		retourErrorSender.sendError(retourNo, message);
	}
}
