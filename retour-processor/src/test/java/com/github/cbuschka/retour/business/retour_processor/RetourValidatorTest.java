package com.github.cbuschka.retour.business.retour_processor;

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

	private void whenValidated(ReceiveRetourCommand message) throws RetourMessageInvalid {
		this.validator.validate(message);
	}

	@Test
	public void retourNoRequired() throws RetourMessageInvalid {
		expectedException.expect(RetourMessageInvalid.class);

		whenValidated(new ReceiveRetourCommand(null, "orderNo"));
	}

	@Test
	public void detectsEmptyRetourNo() throws RetourMessageInvalid {
		expectedException.expect(RetourMessageInvalid.class);

		whenValidated(new ReceiveRetourCommand("", "orderNo"));
	}


	@Test
	public void detectsEmptyOrderNo() throws RetourMessageInvalid {
		expectedException.expect(RetourMessageInvalid.class);

		whenValidated(new ReceiveRetourCommand("retourNo", ""));
	}

	@Test
	public void minimalMessage() throws RetourMessageInvalid {
		whenValidated(new ReceiveRetourCommand("R1","O1234"));
	}
}
