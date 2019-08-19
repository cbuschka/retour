package com.github.cbuschka.retour.domain.order_store;

import com.github.cbuschka.retour.infrastructure.persistence.AggregateRoot;
import com.github.cbuschka.retour.infrastructure.persistence.DynamodbAggregateStore;

public class OrderRepository extends DynamodbAggregateStore<Order>
{
	private static final String TABLE_NAME = "Order";
	private static final String KEY_COLUMN_NAME = "OrderNo";

	public OrderRepository()
	{
		super(TABLE_NAME, KEY_COLUMN_NAME, Order.class);
	}

	public AggregateRoot<Order> findByOrderNo(String orderNo) throws OrderNotFound
	{
		return findByKey(orderNo).orElseThrow(() -> new OrderNotFound(orderNo));
	}
}
