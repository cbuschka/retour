package com.github.cbuschka.retour.retour_processor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;

public class ProcessRetourMessageValidator
{

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	public void failIfInvalid(ProcessRetourMessage message)
	{
		Objects.requireNonNull(message, "Message is missing.");

		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ProcessRetourMessage>> violations = validator.validate(message);
		if (!violations.isEmpty())
		{
			ConstraintViolation<ProcessRetourMessage> firstViolation = violations.iterator().next();
			throw new IllegalArgumentException(firstViolation.getPropertyPath() + " " + firstViolation.getMessage());
		}
	}
}
