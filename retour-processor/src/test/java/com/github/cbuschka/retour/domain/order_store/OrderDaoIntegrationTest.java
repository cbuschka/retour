package com.github.cbuschka.retour.domain.order_store;

import com.github.cbuschka.retour.infrastructure.persistence.AggregateRoot;
import com.github.cbuschka.retour.infrastructure.persistence.ConcurrentModification;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderDaoIntegrationTest
{
	private static final String ORDER_NO = "O1234";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private OrderRepository orderRepository = new OrderRepository();

	@Test
	public void doesNotFindNonExistentOrder()
	{
		Optional<AggregateRoot<Order>> optionalOrderRoot = orderRepository.findByKey("DOES_NOT_EXIST");
		assertThat(optionalOrderRoot.isPresent(), is(false));
	}

	@Test
	public void findsWhatIsStored()
	{
		createPersistentOrder(ORDER_NO);

		AggregateRoot<Order> orderRoot = this.orderRepository.findByKey(ORDER_NO).get();
		assertThat(orderRoot.getKey(), is(ORDER_NO));
	}


	@Test
	public void detectsConcurrentModification()
	{
		createPersistentOrder(ORDER_NO);

		AggregateRoot<Order> orderRoot1 = this.orderRepository.findByKey(ORDER_NO).get();
		AggregateRoot<Order> orderRoot2 = this.orderRepository.findByKey(ORDER_NO).get();
		this.orderRepository.store(orderRoot1);

		expectedException.expect(ConcurrentModification.class);
		this.orderRepository.store(orderRoot2);
	}

	private void createPersistentOrder(String orderNo)
	{
		AggregateRoot<Order> orderRoot = this.orderRepository.findByKey(orderNo)
				.orElseGet(() -> this.orderRepository.create(orderNo));
		orderRepository.store(orderRoot);
	}

}
