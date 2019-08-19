package com.github.cbuschka.retour.domain.order_store;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private static final String RETOUR_NO = "R1";

	private Order order;

	@Test
	public void detectsDupCreatedRetours() throws RetourAlreadyExists
	{
		givenIsAnOrderWithOpenRetour();

		expectedException.expect(RetourAlreadyExists.class);
		order.createRetour(RETOUR_NO);
	}

	@Test
	public void detectsUnknownRetours() throws RetourNotKnown, RetourAlreadyReceived
	{
		givenIsAnOrderWithoutRetours();

		expectedException.expect(RetourNotKnown.class);
		order.receiveRetour(RETOUR_NO);
	}

	@Test
	public void detectsDupReceivedRetours() throws RetourNotKnown, RetourAlreadyReceived, RetourAlreadyExists
	{
		givenIsAnOrderWithReceivedRetour();

		expectedException.expect(RetourAlreadyReceived.class);
		order.receiveRetour(RETOUR_NO);
	}

	@Test
	public void receiveRetour() throws RetourAlreadyReceived, RetourNotKnown, RetourAlreadyExists
	{
		givenIsAnOrderWithReceivedRetour();

		assertThat(order.isRetourReceived(RETOUR_NO), is(true));
	}

	@Test
	public void createRetour() throws RetourAlreadyExists
	{
		givenIsAnOrderWithOpenRetour();

		assertThat(order.isRetourOpen(RETOUR_NO), is(true));
	}

	private void givenIsAnOrderWithOpenRetour() throws RetourAlreadyExists
	{
		this.order = new Order();
		order.createRetour(RETOUR_NO);
	}

	private void givenIsAnOrderWithReceivedRetour() throws RetourAlreadyExists, RetourAlreadyReceived, RetourNotKnown
	{
		this.order = new Order();
		order.createRetour(RETOUR_NO);
		order.receiveRetour(RETOUR_NO);
	}

	private void givenIsAnOrderWithoutRetours()
	{
		this.order = new Order();
	}
}
