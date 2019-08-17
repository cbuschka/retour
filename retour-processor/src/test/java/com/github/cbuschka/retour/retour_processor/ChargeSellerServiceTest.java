package com.github.cbuschka.retour.retour_processor;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

public class ChargeSellerServiceTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private SqsJsonMessageSender<Map> sqsJsonMessageSender;
	@InjectMocks
	private ChargeSellerService chargeSellerService;

	@Test
	public void sendsSqsMessage() {

		whenChargingSellerRequested();

		thenMessageIsSentToChargeSellerQueue();
	}

	private void whenChargingSellerRequested() {
		this.chargeSellerService.chargeSeller();
	}

	private void thenMessageIsSentToChargeSellerQueue() {
		verify(this.sqsJsonMessageSender).send(eq(ChargeSellerService.QUEUE_NAME), any(Map.class));
	}

}