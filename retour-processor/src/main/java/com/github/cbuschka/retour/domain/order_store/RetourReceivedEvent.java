package com.github.cbuschka.retour.domain.order_store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.codestickers.Used;

public class RetourReceivedEvent extends OrderEvent
{
	@JsonProperty
	private String retourNo;

	@JsonProperty
	private String processedAt;

	@Used("Required by jackson.")
	public RetourReceivedEvent()
	{
	}

	public RetourReceivedEvent(String retourNo, String processedAt)
	{
		this.retourNo = retourNo;
		this.processedAt = processedAt;
	}


	@Override
	public String getType()
	{
		return "RetourReceived";
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
