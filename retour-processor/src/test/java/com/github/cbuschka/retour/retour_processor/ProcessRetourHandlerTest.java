package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProcessRetourHandlerTest {

	private static final String EVENT_TO_STRING_FORM = "eventAsString";
	private static final String RETOUR_NO = "retourNo";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private Context context;
	@Mock
	private ChargeSellerService chargeSellerService;
	@Mock
	private LambdaLogger logger;
	@Mock
	private ProcessRetourMessage event;
	@Mock
	private RefundBuyerService refundBuyerService;
	@InjectMocks
	private ProcessRetourHandler processRetourHandler;
	@Mock
	private ProcessRetourMessageValidator processRetourMessageValidator;

	private Object response = "RESPONSE_NOT_TOUCHED";

	@Before
	public void before() {
		givenIsALambdaContextWithLogger();
	}

	@Test
	public void chargesSeller() {
		givenIsAnValidEvent();

		whenHandlerInvoked();

		thenSellerIsCharged();
		thenBuyerIsRefunded();
		thenHandlerReturnedNoValue();
	}

	@Test
	public void doesNotReturnAnything() {
		whenHandlerInvoked();

		thenHandlerReturnedNoValue();
	}

	private void givenIsALambdaContextWithLogger() {
		when(context.getLogger()).thenReturn(logger);
	}

	private void whenHandlerInvoked() {
		this.response = this.processRetourHandler.handleRequest(event, context);
	}

	private void thenSellerIsCharged() {
		verify(chargeSellerService).chargeSeller(RETOUR_NO);
	}

	private void thenHandlerReturnedNoValue() {
		assertThat(this.response, is(nullValue()));
	}

	private void givenIsAnValidEvent() {
		when(event.getRetourNo()).thenReturn(RETOUR_NO);
		when(event.toString()).thenReturn(EVENT_TO_STRING_FORM);
	}

	private void thenBuyerIsRefunded() {
		verify(refundBuyerService).refundBuyer(RETOUR_NO);
	}

}
