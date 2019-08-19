package com.github.cbuschka.retour.domain.order_store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OrderTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private static final String RETOUR_NO = "R1";

	private Order order = new Order();

	@Test
	public void detectsDupRetours() throws RetourAlreadyProcessed
	{
		order.createRetour(RETOUR_NO);

		expectedException.expect(RetourAlreadyProcessed.class);
		order.createRetour(RETOUR_NO);
	}

	@Test
	public void processesRetours() throws RetourAlreadyProcessed
	{
		order.createRetour(RETOUR_NO);

		assertThat(order.isRetourAlreadyProcessed(RETOUR_NO), is(true));
	}

}
