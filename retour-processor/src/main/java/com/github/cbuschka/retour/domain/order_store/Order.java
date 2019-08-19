package com.github.cbuschka.retour.domain.order_store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.cbuschka.retour.util.Dates;
import com.github.codestickers.Note;
import com.github.codestickers.Used;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Note("Order in the bounded context of retour processing.")
public class Order
{
	@JsonProperty
	private List<OrderEvent> events = new ArrayList<>();

	@Used("Required by jackson.")
	public Order()
	{
	}

	public Order(List<OrderEvent> events)
	{
		this.events = events;
	}

	public void createRetour(String retourNo) throws RetourAlreadyExists
	{
		Map<String, RetourStatus> retourStatus = getRetourStatus();
		if (retourStatus.containsKey(retourNo))
		{
			throw new RetourAlreadyExists(retourNo);
		}

		this.events.add(new RetourCreatedEvent(retourNo, Dates.nowUTCAsIsoString()));
	}

	public void receiveRetour(String retourNo) throws RetourAlreadyReceived, RetourNotKnown
	{
		Map<String, RetourStatus> retourStatus = getRetourStatus();
		if (!retourStatus.containsKey(retourNo))
		{
			throw new RetourNotKnown(retourNo);
		}

		if (retourStatus.get(retourNo) == RetourStatus.RECEIVED)
		{
			throw new RetourAlreadyReceived(retourNo);
		}

		this.events.add(new RetourReceivedEvent(retourNo, Dates.nowUTCAsIsoString()));
	}

	private Map<String, RetourStatus> getRetourStatus()
	{
		Map<String, RetourStatus> openRetours = new HashMap<>();
		for (OrderEvent ev : this.events)
		{
			if (ev instanceof RetourCreatedEvent)
			{
				openRetours.put(((RetourCreatedEvent) ev).getRetourNo(), RetourStatus.OPEN);
			}
			else if (ev instanceof RetourReceivedEvent)
			{
				openRetours.put(((RetourReceivedEvent) ev).getRetourNo(), RetourStatus.RECEIVED);
			}
			else
			{
				throw new IllegalArgumentException("Unknown event type: " + ev);
			}
		}

		return openRetours;
	}

	public boolean isRetourOpen(String retourNo)
	{
		return getRetourStatus().get(retourNo) == RetourStatus.OPEN;
	}


	public boolean isRetourReceived(String retourNo)
	{
		return getRetourStatus().get(retourNo) == RetourStatus.RECEIVED;
	}
}
