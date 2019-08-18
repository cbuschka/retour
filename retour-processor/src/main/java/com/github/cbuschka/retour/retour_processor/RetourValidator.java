package com.github.cbuschka.retour.retour_processor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;

public class RetourValidator
{
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	public RetourValidationResult getViolations(RetourMessage message)
	{
		Objects.requireNonNull(message, "Message is missing.");

		Validator validator = factory.getValidator();
		Set<ConstraintViolation<RetourMessage>> violations = validator.validate(message);
		return new RetourValidationResult(violations);
	}
}
