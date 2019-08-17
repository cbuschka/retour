package com.github.cbuschka.retour;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class AssumptionsTest {
	@Test
	public void java8Only() {
		String javaVersion = System.getProperty("java.version");

		assertThat(javaVersion, startsWith("1.8."));
	}
}