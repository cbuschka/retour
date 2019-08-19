package com.github.cbuschka.retour.domain.order_store;

import com.github.codestickers.Used;

public class RetourCreatedEvent
{
	private String retourNo;

	private String processedAt;

	@Used("Required by jackson.")
	public RetourCreatedEvent()
	{
	}

	public RetourCreatedEvent(String retourNo, String processedAt)
	{
		this.retourNo = retourNo;
		this.processedAt = processedAt;
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
