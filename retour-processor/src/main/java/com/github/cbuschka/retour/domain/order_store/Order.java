package com.github.cbuschka.retour.domain.order_store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.cbuschka.retour.util.Dates;
import com.github.codestickers.Note;
import com.github.codestickers.Used;

import java.util.ArrayList;
import java.util.List;

@Note("Order in the bounded context of retour processing.")
public class Order
{
	@JsonProperty
	private List<RetourCreatedEvent> events = new ArrayList<>();

	@Used("Required by jackson.")
	public Order()
	{
	}

	public Order(List<RetourCreatedEvent> events)
	{
		this.events = events;
	}

	public void createRetour(String retourNo) throws RetourAlreadyProcessed
	{
		if (isRetourAlreadyProcessed(retourNo))
		{
			throw new RetourAlreadyProcessed("Retour retourNo=" + retourNo + " already processed.");
		}

		this.events.add(new RetourCreatedEvent(retourNo, Dates.nowUTCAsIsoString()));
	}

	public boolean isRetourAlreadyProcessed(String retourNo)
	{
		return this.events.stream().anyMatch((e) -> e.getRetourNo().equals(retourNo));
	}
}
