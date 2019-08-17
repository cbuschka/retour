package com.github.cbuschka.retour;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class AssumptionsTest {
	@Test
	public void java8Only() {
		String javaVersion = System.getProperty("java.version");

		assertThat(javaVersion, startsWith("1.8."));
	}

	@Test
	public void emptyMapSerializes() throws JsonProcessingException {
		String json = new ObjectMapper().writeValueAsString(Collections.emptyMap());

		assertThat(json, is("{}"));
	}
}