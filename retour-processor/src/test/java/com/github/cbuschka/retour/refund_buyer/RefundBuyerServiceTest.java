package com.github.cbuschka.retour.refund_buyer;

import com.github.cbuschka.retour.sqs.SqsJsonMessageSender;
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

public class RefundBuyerServiceTest {

	private static final String RETOUR_NO = "retourNo";

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private SqsJsonMessageSender<RefundBuyerMessage> sqsJsonMessageSender;
	@InjectMocks
	private RefundBuyerService refundBuyerService;

	@Test
	public void sendsSqsMessage() {

		whenRefundingBuyerRequested();

		thenMessageIsSentToRefundBuyerQueue();
	}

	private void whenRefundingBuyerRequested() {
		this.refundBuyerService.refundBuyer(RETOUR_NO);
	}

	private void thenMessageIsSentToRefundBuyerQueue() {
		verify(this.sqsJsonMessageSender).send(eq(RefundBuyerService.QUEUE_NAME), any(RefundBuyerMessage.class), eq(RETOUR_NO));
	}

}
