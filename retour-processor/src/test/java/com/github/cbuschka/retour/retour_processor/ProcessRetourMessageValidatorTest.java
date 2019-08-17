package com.github.cbuschka.retour.retour_processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ProcessRetourMessageValidatorTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private ProcessRetourMessageValidator validator = new ProcessRetourMessageValidator();

	@Test
	public void messageRequired() {
		expectedException.expect(NullPointerException.class);

		this.validator.failIfInvalid(null);
	}

	@Test
	public void retourNoRequired() {
		expectedException.expect(NullPointerException.class);

		this.validator.failIfInvalid(new ProcessRetourMessage(null));
	}

	@Test
	public void detectsEmptyRetourNo() {
		expectedException.expect(IllegalArgumentException.class);

		this.validator.failIfInvalid(new ProcessRetourMessage(""));
	}
}