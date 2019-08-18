package com.github.cbuschka.retour.retour_processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RetourValidatorTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private RetourValidator validator = new RetourValidator();

	private RetourValidationResult validationResult;

	@Test
	public void messageRequired()
	{
		expectedException.expect(NullPointerException.class);

		whenValidated(null);

		thenNotValid();
	}

	private void whenValidated(RetourMessage message)
	{
		this.validationResult = this.validator.getViolations(message);
	}

	private void thenNotValid()
	{
		assertThat(this.validationResult.isValid(), is(false));
	}

	@Test
	public void retourNoRequired()
	{
		whenValidated(new RetourMessage(null));

		thenNotValid();
	}

	@Test
	public void detectsEmptyRetourNo()
	{
		whenValidated(new RetourMessage(""));

		thenNotValid();
	}

	@Test
	public void minimalMessage()
	{
		whenValidated(new RetourMessage("R1"));

		thenValid();
	}

	private void thenValid()
	{
		assertThat(this.validationResult.isValid(), is(true));
	}
}
