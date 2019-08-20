package com.github.cbuschka.retour.domain.order_store;

import com.github.cbuschka.retour.RetourProcessorConfig;
import com.github.cbuschka.retour.infrastructure.persistence.AggregateRoot;
import com.github.cbuschka.retour.infrastructure.persistence.ConcurrentModification;
import com.github.cbuschka.retour.infrastructure.spring.ApplicationContextFactory;
import com.github.cbuschka.retour.util.Dates;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderRepositoryIntegrationTest
{
	private String randomNo = Long.toHexString(System.currentTimeMillis());
	private String orderNo = "O" + randomNo;
	private String retourNo = "R" + randomNo;
	private static final String UNKNOWN_ORDER_NO = "DOES_NOT_EXIST";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Autowired
	private OrderRepository orderRepository;

	private AnnotationConfigApplicationContext applicationContext;

	@Before
	public void before()
	{
		this.applicationContext = ApplicationContextFactory.newApplicationContext(RetourProcessorConfig.class);
		this.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
	}

	@After
	public void after()
	{
		this.applicationContext.close();
	}

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
	public void findsWhatIsStored() throws RetourAlreadyExists
	{
		createPersistentOrder(orderNo);

		AggregateRoot<Order> orderRoot = this.orderRepository.findByKey(orderNo).get();
		assertThat(orderRoot.getKey(), is(orderNo));
	}

	@Test
	public void saveAndRereadNewOne() throws RetourAlreadyExists
	{
		String orderNo = "OTEST" + Dates.nowUTCAsIsoString();
		createPersistentOrder(orderNo);

		AggregateRoot<Order> orderRoot = this.orderRepository.findByKey(orderNo).get();
		assertThat(orderRoot.getKey(), is(orderNo));
	}

	@Test
	public void detectsConcurrentModification() throws RetourAlreadyExists
	{
		createPersistentOrder(orderNo);

		AggregateRoot<Order> orderRoot1 = this.orderRepository.findByKey(orderNo).get();
		AggregateRoot<Order> orderRoot2 = this.orderRepository.findByKey(orderNo).get();
		this.orderRepository.store(orderRoot1);

		expectedException.expect(ConcurrentModification.class);
		this.orderRepository.store(orderRoot2);
	}

	private void createPersistentOrder(String orderNo) throws RetourAlreadyExists
	{
		AggregateRoot<Order> orderRoot = this.orderRepository.findByKey(orderNo)
				.orElseGet(() -> this.orderRepository.create(orderNo));
		orderRoot.getData().createRetour(retourNo);
		orderRepository.store(orderRoot);
	}

	@Test
	public void createAndStoreOrderWithSingleOpenRetour() throws RetourAlreadyExists
	{
		AggregateRoot<Order> orderRoot = this.orderRepository.create(orderNo);
		orderRoot.getData().createRetour(retourNo);
		orderRepository.store(orderRoot);

		System.err.println(orderNo);
	}

}
