package com.github.cbuschka.retour.domain.retour_store;

public class RetourAlreadyProcessed extends Exception
{
	public RetourAlreadyProcessed(String message) {
		super(message);
	}
}
