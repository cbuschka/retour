package com.github.cbuschka.retour.retour_processor;

import java.util.Objects;

public class ProcessRetourMessageValidator {

	public void failIfInvalid(ProcessRetourMessage message) {
		Objects.requireNonNull(message, "Message is missing.");
		Objects.requireNonNull(message.getRetourNo(), "RetourNo missing.");
		if (message.getRetourNo().isEmpty()) {
			throw new IllegalArgumentException("RetourNo must not be empty.");
		}
	}
}
