package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.domain.charge_seller.ChargeSellerService;
import com.github.cbuschka.retour.domain.order_store.Order;
import com.github.cbuschka.retour.domain.order_store.OrderNotFound;
import com.github.cbuschka.retour.domain.order_store.OrderRepository;
import com.github.cbuschka.retour.domain.order_store.RetourAlreadyProcessed;
import com.github.cbuschka.retour.domain.refund_buyer.RefundBuyerService;
import com.github.cbuschka.retour.infrastructure.persistence.AggregateRoot;
import com.github.cbuschka.retour.util.Logger;

public class RetourProcessor
{
	private static Logger logger = Logger.get();

	private RetourValidator retourValidator = new RetourValidator();

	private ChargeSellerService chargeSellerService = new ChargeSellerService();

	private RefundBuyerService refundBuyerService = new RefundBuyerService();

	private RetourErrorSender retourErrorSender = new RetourErrorSender();

	private RetourAckSender retourAckSender = new RetourAckSender();

	private OrderRepository orderRepository = new OrderRepository();

	public void processRetour(RetourMessage message)
	{
		logger.log("Processing " + message + "...");

		try
		{
			retourValidator.validate(message);

			AggregateRoot<Order> orderRoot = orderRepository.findByOrderNo(message.getOrderNo());
			Order order = orderRoot.getData();

			String retourNo = message.getRetourNo();
			order.createRetour(retourNo);

			chargeSellerService.chargeSeller(retourNo);
			refundBuyerService.refundBuyer(retourNo);
			retourAckSender.sendAck(retourNo);

			orderRepository.store(orderRoot);
		}
		catch (RetourMessageInvalid | RetourAlreadyProcessed | OrderNotFound ex)
		{
			sendErrorMessage(message.getRetourNo(), ex);
		}

		logger.log(message + " processed.");
	}

	private void sendErrorMessage(String retourNo, Exception ex)
	{
		logger.log(ex.getMessage());
		retourErrorSender.sendError(retourNo, ex);
	}
}
