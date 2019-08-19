package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.domain.charge_seller.ChargeSellerService;
import com.github.cbuschka.retour.domain.order_store.Order;
import com.github.cbuschka.retour.domain.order_store.OrderNotFound;
import com.github.cbuschka.retour.domain.order_store.OrderRepository;
import com.github.cbuschka.retour.domain.order_store.RetourAlreadyReceived;
import com.github.cbuschka.retour.domain.order_store.RetourNotKnown;
import com.github.cbuschka.retour.domain.refund_buyer.RefundBuyerService;
import com.github.cbuschka.retour.infrastructure.persistence.AggregateRoot;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class RetourProcessorTest
{
	private static final String RETOUR_NO = "retourNo";
	private static final String ORDER_NO = "orderNo";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private ChargeSellerService chargeSellerService;
	@Mock
	private AggregateRoot<Order> orderAggregateRoot;
	@Mock
	private Order order;
	@Mock
	private ReceiveRetourCommand receiveRetourCommand;
	@Mock
	private RefundBuyerService refundBuyerService;
	@InjectMocks
	private RetourProcessor retourProcessor;
	@Mock
	private RetourValidator retourValidator;
	@Mock
	private RetourErrorSender retourErrorSender;
	@Mock
	private RetourAckSender retourAckSender;
	@Mock
	private OrderRepository orderRepository;

	@Test
	public void chargesSeller() throws RetourAlreadyReceived, OrderNotFound, RetourNotKnown
	{
		givenIsAnValidRetourMessage();
		givenIsACorrespondingOrder();

		whenHandlerInvoked();

		thenRetourRecordIReceived();
		thenSellerIsCharged();
		thenBuyerIsRefunded();
		thenNoErrorIsSent();
		thenAckIsSent();
	}

	private void givenIsACorrespondingOrder() throws OrderNotFound
	{
		when(this.orderRepository.findByOrderNo(ORDER_NO)).thenReturn(orderAggregateRoot);
		when(this.orderAggregateRoot.getData()).thenReturn(this.order);
	}

	private void thenRetourRecordIReceived() throws RetourAlreadyReceived, RetourNotKnown
	{
		verify(this.order).receiveRetour(RETOUR_NO);
	}

	@Test
	public void sendErrorWhenRetourInvalid() throws RetourMessageInvalid, OrderNotFound
	{
		givenIsAnInvalidRetourMessage();
		givenIsACorrespondingOrder();

		whenHandlerInvoked();

		thenRetourRecordIsNotCreated();
		thenSellerIsNotCharged();
		thenBuyerIsNotRefunded();
		thenErrorIsSent();
		thenAckIsNotSent();
	}


	@Test
	public void sendErrorWhenOrderIsUnknown() throws OrderNotFound
	{
		givenIsAnValidRetourMessage();
		givenIsAnUnknownOrder();

		whenHandlerInvoked();

		thenRetourRecordIsNotCreated();
		thenSellerIsNotCharged();
		thenBuyerIsNotRefunded();
		thenUnknownOrderErrorIsSent();
		thenAckIsNotSent();
	}

	private void givenIsAnUnknownOrder() throws OrderNotFound
	{
		doThrow(new OrderNotFound(ORDER_NO)).when(this.orderRepository).findByOrderNo(ORDER_NO);
	}

	private void thenRetourRecordIsNotCreated()
	{
		verifyZeroInteractions(this.order);
	}

	private void thenAckIsNotSent()
	{
		verifyZeroInteractions(this.retourAckSender);
	}

	private void thenErrorIsSent()
	{
		verify(this.retourErrorSender).sendError(eq(RETOUR_NO), any(RetourMessageInvalid.class));
	}

	private void thenUnknownOrderErrorIsSent()
	{
		verify(this.retourErrorSender).sendError(eq(RETOUR_NO), any(OrderNotFound.class));
	}

	private void thenBuyerIsNotRefunded()
	{
		verifyZeroInteractions(this.refundBuyerService);
	}

	private void thenSellerIsNotCharged()
	{
		verifyZeroInteractions(this.chargeSellerService);
	}

	private void whenHandlerInvoked()
	{
		this.retourProcessor.processRetour(receiveRetourCommand);
	}

	private void thenSellerIsCharged()
	{
		verify(chargeSellerService).chargeSeller(RETOUR_NO);
	}

	private void givenIsAnValidRetourMessage()
	{
		when(receiveRetourCommand.getRetourNo()).thenReturn(RETOUR_NO);
		when(receiveRetourCommand.getOrderNo()).thenReturn(ORDER_NO);
	}

	private void thenAckIsSent()
	{
		verify(this.retourAckSender).sendAck(RETOUR_NO);
	}

	private void givenIsAnInvalidRetourMessage() throws RetourMessageInvalid
	{
		doThrow(new RetourMessageInvalid(RETOUR_NO)).when(this.retourValidator).validate(receiveRetourCommand);
		when(receiveRetourCommand.getRetourNo()).thenReturn(RETOUR_NO);
	}

	private void thenBuyerIsRefunded()
	{
		verify(refundBuyerService).refundBuyer(RETOUR_NO);
	}

	private void thenNoErrorIsSent()
	{
		verifyZeroInteractions(this.retourErrorSender);
	}

}
