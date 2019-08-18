package com.github.cbuschka.retour.retour_processor;

import com.github.cbuschka.retour.charge_seller.ChargeSellerService;
import com.github.cbuschka.retour.refund_buyer.RefundBuyerService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class RetourProcessorTest
{
	private static final String RETOUR_NO = "retourNo";
	private static final String ERROR_MESSAGE = "errorMessage";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private ChargeSellerService chargeSellerService;
	@Mock
	private RetourMessage event;
	@Mock
	private RefundBuyerService refundBuyerService;
	@InjectMocks
	private RetourProcessor retourProcessor;
	@Mock
	private RetourValidator retourValidator;
	@Mock
	private RetourValidationResult retourValidationResult;
	@Mock
	private RetourErrorSender retourErrorSender;
	@Mock
	private RetourAckSender retourAckSender;

	@Test
	public void chargesSeller()
	{
		givenIsAnValidEvent();

		whenHandlerInvoked();

		thenSellerIsCharged();
		thenBuyerIsRefunded();
		thenNoErrorIsSent();
		thenAckIsSent();
	}

	@Test
	public void sendErrorWhenRetourInvalid()
	{
		givenIsAnInvalidEvent();

		whenHandlerInvoked();

		thenSellerIsNotCharged();
		thenBuyerIsNotRefunded();
		thenErrorIsSent();
		thenAckIsNotSent();
	}

	private void thenAckIsNotSent()
	{
		verifyZeroInteractions(this.retourAckSender);
	}

	private void thenErrorIsSent()
	{
		verify(this.retourErrorSender).sendError(RETOUR_NO, ERROR_MESSAGE);
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
		this.retourProcessor.processRetour(event);
	}

	private void thenSellerIsCharged()
	{
		verify(chargeSellerService).chargeSeller(RETOUR_NO);
	}

	private void givenIsAnValidEvent()
	{
		when(this.retourValidator.getViolations(event)).thenReturn(this.retourValidationResult);
		when(event.getRetourNo()).thenReturn(RETOUR_NO);
		when(this.retourValidationResult.isValid()).thenReturn(true);
	}

	private void thenAckIsSent()
	{
		verify(this.retourAckSender).sendAck(RETOUR_NO);
	}

	private void givenIsAnInvalidEvent()
	{
		when(this.retourValidator.getViolations(event)).thenReturn(this.retourValidationResult);
		when(event.getRetourNo()).thenReturn(RETOUR_NO);
		when(this.retourValidationResult.isValid()).thenReturn(false);
		when(this.retourValidationResult.toMessage()).thenReturn(ERROR_MESSAGE);
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
