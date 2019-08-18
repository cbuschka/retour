package com.github.cbuschka.retour.retour_processor;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class RetourValidationResult
{
	private Set<ConstraintViolation<RetourMessage>> violations;

	public RetourValidationResult(Set<ConstraintViolation<RetourMessage>> violations)
	{
		this.violations = violations;
	}

	public String toMessage()
	{
		return this.violations.toString();
	}

	public boolean isValid()
	{
		return this.violations.isEmpty();
	}
}
