package com.github.cbuschka.retour.infrastructure.persistence;

public class ConcurrentModification extends RuntimeException
{
	public ConcurrentModification(String message)
	{
		super(message);
	}
}
