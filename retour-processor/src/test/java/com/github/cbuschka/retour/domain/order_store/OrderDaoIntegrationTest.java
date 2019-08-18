package com.github.cbuschka.retour.domain.order_store;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class OrderDaoIntegrationTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private OrderDao orderDao = new OrderDao();

	@Test
	public void doesNotFindNonExistentOrder() throws OrderNotFound
	{
		expectedException.expect(OrderNotFound.class);

		orderDao.findOrder("DOES_NOT_EXIST");
	}


	@Test
	public void findsWhatIsStored() throws OrderNotFound
	{
		String orderNo = "O1234";
		orderDao.storeOrder(new OrderRecord(orderNo));

		OrderRecord orderFound = orderDao.findOrder(orderNo);

		assertThat(orderFound.orderNo, is(orderNo));
	}

}
