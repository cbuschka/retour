package com.github.cbuschka.retour.domain.order_store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.codestickers.Used;

public class RetourCreatedEvent extends OrderEvent
{
	@JsonProperty
	private String retourNo;

	@JsonProperty
	private String processedAt;

	@Used("Required by jackson.")
	public RetourCreatedEvent()
	{
	}

	@Override
	public String getType()
	{
		return "RetourCreated";
	}

	public RetourCreatedEvent(String retourNo, String processedAt)
	{
		this.processedAt = processedAt;
		this.retourNo = retourNo;
	}

	public String getRetourNo()
	{
		return retourNo;
	}

	public String getProcessedAt()
	{
		return processedAt;
	}
}
