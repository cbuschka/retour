package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.RetourProcessorApp;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class RetourProcessorRequestHandler extends SpringBootRequestHandler<RetourMessage, Void> {

	public RetourProcessorRequestHandler() {
		super(RetourProcessorApp.class);
	}
}