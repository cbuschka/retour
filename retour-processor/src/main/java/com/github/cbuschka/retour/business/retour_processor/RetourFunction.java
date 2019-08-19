package com.github.cbuschka.retour.business.retour_processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RetourFunction implements Function<RetourMessage, Void> {

	@Autowired
	private RetourProcessor retourProcessor;

	@Override
	public Void apply(RetourMessage message) {
		this.retourProcessor.processRetour(message);
		return null;
	}
}