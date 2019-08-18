package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetourProcessorTest
{
	private static final String EVENT_TO_STRING_FORM = "eventAsString";
	private static final String RETOUR_NO = "retourNo";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private ChargeSellerService chargeSellerService;
	@Mock
	private LambdaLogger logger;
	@Mock
	private ProcessRetourMessage event;
	@Mock
	private RefundBuyerService refundBuyerService;
	@InjectMocks
	private RetourProcessor retourProcessor;
	@Mock
	private ProcessRetourMessageValidator processRetourMessageValidator;

	@Test
	public void chargesSeller() {
		givenIsAnValidEvent();

		whenHandlerInvoked();

		thenSellerIsCharged();
		thenBuyerIsRefunded();
	}

	private void whenHandlerInvoked() {
		this.retourProcessor.processRetour(event);
	}

	private void thenSellerIsCharged() {
		verify(chargeSellerService).chargeSeller(RETOUR_NO);
	}

	private void givenIsAnValidEvent() {
		when(event.getRetourNo()).thenReturn(RETOUR_NO);
		when(event.toString()).thenReturn(EVENT_TO_STRING_FORM);
	}

	private void thenBuyerIsRefunded() {
		verify(refundBuyerService).refundBuyer(RETOUR_NO);
	}

}
