package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.services.lambda.runtime.Context;
import com.github.cbuschka.retour.lambda.SqsAwareLambdaHandler;

public class RetourLambdaHandler extends SqsAwareLambdaHandler<RetourMessage> {

	private RetourProcessor retourProcessor = new RetourProcessor();

	public RetourLambdaHandler() {
		super(RetourMessage.class);
	}

	@Override
	protected void handle(RetourMessage message, Context context) {
		this.retourProcessor.processRetour(message);
	}
}