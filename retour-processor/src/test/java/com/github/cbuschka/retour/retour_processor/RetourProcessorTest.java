package com.github.cbuschka.retour.retour_processor;

import com.github.cbuschka.retour.charge_seller.ChargeSellerService;
import com.github.cbuschka.retour.refund_buyer.RefundBuyerService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

public class RetourProcessorTest {
	private static final String RETOUR_NO = "retourNo";
	private static final String ERROR_MESSAGE = "errorMessage";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private ChargeSellerService chargeSellerService;
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

	@Test
	public void chargesSeller() throws RetourAlreadyProcessed {
		givenIsAnValidRetourMessage();

		whenHandlerInvoked();

		thenRetourRecordIsCreated();
		thenSellerIsCharged();
		thenBuyerIsRefunded();
		thenNoErrorIsSent();
		thenAckIsSent();
	}

	private void thenRetourRecordIsCreated() throws RetourAlreadyProcessed {
		verify(this.retourDao).createRetour(RETOUR_NO);
	}

	@Test
	public void sendErrorWhenRetourInvalid() throws RetourMessageInvalid {
		givenIsAnInvalidRetourMessage();

		whenHandlerInvoked();

		thenRetourRecordIsNotCreated();
		thenSellerIsNotCharged();
		thenBuyerIsNotRefunded();
		thenErrorIsSent();
		thenAckIsNotSent();
	}

	private void thenRetourRecordIsNotCreated() {
		verifyZeroInteractions(this.retourDao);
	}

	private void thenAckIsNotSent() {
		verifyZeroInteractions(this.retourAckSender);
	}

	private void thenErrorIsSent() {
		verify(this.retourErrorSender).sendError(RETOUR_NO, ERROR_MESSAGE);
	}

	private void thenBuyerIsNotRefunded() {
		verifyZeroInteractions(this.refundBuyerService);
	}

	private void thenSellerIsNotCharged() {
		verifyZeroInteractions(this.chargeSellerService);
	}

	private void whenHandlerInvoked() {
		this.retourProcessor.processRetour(retourMessage);
	}

	private void thenSellerIsCharged() {
		verify(chargeSellerService).chargeSeller(RETOUR_NO);
	}

	private void givenIsAnValidRetourMessage() {
		when(retourMessage.getRetourNo()).thenReturn(RETOUR_NO);
	}

	private void thenAckIsSent() {
		verify(this.retourAckSender).sendAck(RETOUR_NO);
	}

	private void givenIsAnInvalidRetourMessage() throws RetourMessageInvalid {
		doThrow(new RetourMessageInvalid(ERROR_MESSAGE)).when(this.retourValidator).validate(retourMessage);
		when(retourMessage.getRetourNo()).thenReturn(RETOUR_NO);
	}

	private void thenBuyerIsRefunded() {
		verify(refundBuyerService).refundBuyer(RETOUR_NO);
	}

	private void thenNoErrorIsSent() {
		verifyZeroInteractions(this.retourErrorSender);
	}

}
