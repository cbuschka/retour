package com.github.cbuschka.retour.domain.order_store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.cbuschka.retour.util.Dates;
import com.github.codestickers.Used;

import java.util.ArrayList;
import java.util.List;

public class Order
{
	@JsonProperty
	private List<RetourProcessedEvent> events = new ArrayList<>();

	@Used("Required by jackson.")
	public Order()
	{
	}

	public Order(List<RetourProcessedEvent> events)
	{
		this.events = events;
	}

	public void processRetour(String retourNo) throws RetourAlreadyProcessed
	{
		if (this.events.stream()
				.anyMatch((e) -> e.getRetourNo().equals(retourNo)))
		{
			throw new RetourAlreadyProcessed("Retour retourNo=" + retourNo + " already processed.");
		}

		this.events.add(new RetourProcessedEvent(retourNo, Dates.nowUTCAsIsoString()));
	}
}
