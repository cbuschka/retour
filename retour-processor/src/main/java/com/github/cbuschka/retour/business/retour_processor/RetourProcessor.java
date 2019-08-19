package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.domain.charge_seller.ChargeSellerService;
import com.github.cbuschka.retour.domain.order_store.Order;
import com.github.cbuschka.retour.domain.order_store.OrderNotFound;
import com.github.cbuschka.retour.domain.order_store.OrderRepository;
import com.github.cbuschka.retour.domain.order_store.RetourAlreadyReceived;
import com.github.cbuschka.retour.domain.order_store.RetourNotKnown;
import com.github.cbuschka.retour.domain.refund_buyer.RefundBuyerService;
import com.github.cbuschka.retour.infrastructure.persistence.AggregateRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetourProcessor
{
	private static Logger logger = LoggerFactory.getLogger(RetourProcessor.class);

	private RetourValidator retourValidator = new RetourValidator();

	private ChargeSellerService chargeSellerService = new ChargeSellerService();

	private RefundBuyerService refundBuyerService = new RefundBuyerService();

	private RetourErrorSender retourErrorSender = new RetourErrorSender();

	private RetourAckSender retourAckSender = new RetourAckSender();

	private OrderRepository orderRepository = new OrderRepository();

	public void processRetour(ReceiveRetourCommand message)
	{
		logger.info("Processing " + message + "...");

		try
		{
			retourValidator.validate(message);

			AggregateRoot<Order> orderRoot = orderRepository.findByOrderNo(message.getOrderNo());
			Order order = orderRoot.getData();

			String retourNo = message.getRetourNo();
			order.receiveRetour(retourNo);

			chargeSellerService.chargeSeller(retourNo);
			refundBuyerService.refundBuyer(retourNo);
			retourAckSender.sendAck(retourNo);

			orderRepository.store(orderRoot);
		}
		catch (RetourMessageInvalid | RetourAlreadyReceived | RetourNotKnown | OrderNotFound ex)
		{
			logger.error("Processing {} failed.", message, ex);

			sendErrorMessage(message.getRetourNo(), ex);
		}

		logger.info(message + " processed.");
	}

	private void sendErrorMessage(String retourNo, Exception ex)
	{
		logger.info(ex.getMessage(), ex);
		retourErrorSender.sendError(retourNo, ex);
	}
}
