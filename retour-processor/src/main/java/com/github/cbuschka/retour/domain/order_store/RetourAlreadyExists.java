package com.github.cbuschka.retour.domain.order_store;

public class RetourAlreadyExists extends Exception
{
	public RetourAlreadyExists(String message) {
		super(message);
	}
}
