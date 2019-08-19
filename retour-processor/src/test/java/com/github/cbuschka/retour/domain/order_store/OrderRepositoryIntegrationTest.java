package com.github.cbuschka.retour.domain.order_store;

import com.github.cbuschka.retour.infrastructure.persistence.AggregateRoot;
import com.github.cbuschka.retour.infrastructure.persistence.ConcurrentModification;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderRepositoryIntegrationTest
{
	private static final String ORDER_NO = "O1234";
	private static final String UNKNOWN_ORDER_NO = "DOES_NOT_EXIST";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private OrderRepository orderRepository = new OrderRepository();

	@Test
	public void doesNotFindNonExistentOrder()
	{
		Optional<AggregateRoot<Order>> optionalOrderRoot = orderRepository.findByKey(UNKNOWN_ORDER_NO);
		assertThat(optionalOrderRoot.isPresent(), is(false));
	}

	@Test
	public void throwsOrderNotFoundForNonExistentOrder() throws OrderNotFound
	{
		expectedException.expect(OrderNotFound.class);

		orderRepository.findByOrderNo(UNKNOWN_ORDER_NO);
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
