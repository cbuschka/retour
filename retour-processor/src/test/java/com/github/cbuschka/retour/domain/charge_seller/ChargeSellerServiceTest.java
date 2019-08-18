package com.github.cbuschka.retour.domain.charge_seller;

import com.github.cbuschka.retour.infrastructure.sqs.SqsJsonMessageSender;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

public class ChargeSellerServiceTest {

	private static final String RETOUR_NO = "retourNo";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private SqsJsonMessageSender<ChargeSellerMessage> sqsJsonMessageSender;
	@InjectMocks
	private ChargeSellerService chargeSellerService;

	@Test
	public void sendsSqsMessage() {

		whenChargingSellerRequested();

		thenMessageIsSentToChargeSellerQueue();
	}

	private void whenChargingSellerRequested() {
		this.chargeSellerService.chargeSeller(RETOUR_NO);
	}

	private void thenMessageIsSentToChargeSellerQueue() {
		verify(this.sqsJsonMessageSender).send(eq(ChargeSellerService.QUEUE_NAME), any(ChargeSellerMessage.class), eq(RETOUR_NO));
	}

}
