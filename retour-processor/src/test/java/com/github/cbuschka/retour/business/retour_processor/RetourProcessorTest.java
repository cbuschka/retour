package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.domain.charge_seller.ChargeSellerService;
import com.github.cbuschka.retour.domain.order_store.OrderDao;
import com.github.cbuschka.retour.domain.order_store.OrderNotFound;
import com.github.cbuschka.retour.domain.order_store.OrderRecord;
import com.github.cbuschka.retour.domain.refund_buyer.RefundBuyerService;
import com.github.cbuschka.retour.domain.retour_store.RetourAlreadyProcessed;
import com.github.cbuschka.retour.domain.retour_store.RetourDao;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class RetourProcessorTest
{
	private static final String RETOUR_NO = "retourNo";
	private static final String INVALID_MESSAGE_ERROR_MESSAGE = "invalidMessageErrorMessage";
	private static final String UNKNOWN_ORDER_ERROR_MESSAGE = "unknownOrderErrorMessage";
	private static final String ORDER_NO = "orderNo";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private ChargeSellerService chargeSellerService;
	@Mock
	private OrderRecord orderRecord;
	@Mock
	private RetourMessage retourMessage;
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
	private RetourDao retourDao;
	@Mock
	private OrderDao orderDao;

	@Test
	public void chargesSeller() throws RetourAlreadyProcessed, OrderNotFound
	{
		givenIsAnValidRetourMessage();
		givenIsACorrespondingOrder();

		whenHandlerInvoked();

		thenRetourRecordIsCreated();
		thenSellerIsCharged();
		thenBuyerIsRefunded();
		thenNoErrorIsSent();
		thenAckIsSent();
	}

	private void givenIsACorrespondingOrder() throws OrderNotFound
	{
		when(this.orderDao.findOrder(ORDER_NO)).thenReturn(orderRecord);
	}

	private void thenRetourRecordIsCreated() throws RetourAlreadyProcessed
	{
		verify(this.retourDao).createRetour(RETOUR_NO);
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
	public void sendErrorWhenOrderIsUnknown() throws RetourMessageInvalid, OrderNotFound
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
		when(this.orderDao.findOrder(ORDER_NO)).thenThrow(new OrderNotFound(UNKNOWN_ORDER_ERROR_MESSAGE));
	}

	private void thenRetourRecordIsNotCreated()
	{
		verifyZeroInteractions(this.retourDao);
	}

	private void thenAckIsNotSent()
	{
		verifyZeroInteractions(this.retourAckSender);
	}

	private void thenErrorIsSent()
	{
		verify(this.retourErrorSender).sendError(RETOUR_NO, INVALID_MESSAGE_ERROR_MESSAGE);
	}

	private void thenUnknownOrderErrorIsSent()
	{
		verify(this.retourErrorSender).sendError(RETOUR_NO, UNKNOWN_ORDER_ERROR_MESSAGE);
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
		this.retourProcessor.processRetour(retourMessage);
	}

	private void thenSellerIsCharged()
	{
		verify(chargeSellerService).chargeSeller(RETOUR_NO);
	}

	private void givenIsAnValidRetourMessage()
	{
		when(retourMessage.getRetourNo()).thenReturn(RETOUR_NO);
		when(retourMessage.getOrderNo()).thenReturn(ORDER_NO);
	}

	private void thenAckIsSent()
	{
		verify(this.retourAckSender).sendAck(RETOUR_NO);
	}

	private void givenIsAnInvalidRetourMessage() throws RetourMessageInvalid
	{
		doThrow(new RetourMessageInvalid(INVALID_MESSAGE_ERROR_MESSAGE)).when(this.retourValidator).validate(retourMessage);
		when(retourMessage.getRetourNo()).thenReturn(RETOUR_NO);
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
