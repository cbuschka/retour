package com.github.cbuschka.retour.domain.order_store;

public class RetourAlreadyReceived extends Exception
{
	public RetourAlreadyReceived(String message) {
		super(message);
	}
}
