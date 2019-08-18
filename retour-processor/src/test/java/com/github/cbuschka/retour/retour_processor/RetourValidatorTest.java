package com.github.cbuschka.retour.retour_processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RetourValidatorTest {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private RetourValidator validator = new RetourValidator();

	@Test
	public void messageRequired() throws RetourMessageInvalid {
		expectedException.expect(NullPointerException.class);

		whenValidated(null);
	}

	private void whenValidated(RetourMessage message) throws RetourMessageInvalid {
		this.validator.validate(message);
	}

	@Test
	public void retourNoRequired() throws RetourMessageInvalid {
		expectedException.expect(RetourMessageInvalid.class);

		whenValidated(new RetourMessage(null));
	}

	@Test
	public void detectsEmptyRetourNo() throws RetourMessageInvalid {
		expectedException.expect(RetourMessageInvalid.class);

		whenValidated(new RetourMessage(""));
	}

	@Test
	public void minimalMessage() throws RetourMessageInvalid {
		whenValidated(new RetourMessage("R1"));
	}
}
