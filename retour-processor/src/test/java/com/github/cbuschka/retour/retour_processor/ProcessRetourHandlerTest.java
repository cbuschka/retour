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

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProcessRetourHandlerTest {

	private static final String EVENT_TO_STRING_FORM = "<<EVENT>>";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private Context context;
	@Mock
	private ChargeSellerService chargeSellerService;
	@Mock
	private LambdaLogger logger;
	@Mock
	private Map<String, Object> event;
	@InjectMocks
	private ProcessRetourHandler processRetourHandler;

	private Object response = "RESPONSE_NOT_TOUCHED";

	@Before
	public void before() {
		givenIsALambdaContextWithLogger();
	}

	@Test
	public void chargesSeller() {
		givenIsAnEvent();

		whenHandlerInvoked();

		thenSellerIsCharged();
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
		verify(chargeSellerService).chargeSeller();
	}

	private void thenHandlerReturnedNoValue() {
		assertThat(this.response, is(nullValue()));
	}

	private void givenIsAnEvent() {
		when(event.toString()).thenReturn(EVENT_TO_STRING_FORM);
	}
}