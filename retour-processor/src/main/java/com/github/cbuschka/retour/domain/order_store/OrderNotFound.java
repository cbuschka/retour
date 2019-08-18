package com.github.cbuschka.retour.domain.order_store;

public class OrderNotFound extends Exception
{
	public OrderNotFound(String message) {
		super(message);
	}
}
