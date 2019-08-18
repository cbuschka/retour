package com.github.cbuschka.retour.domain.order_store;

import com.github.codestickers.Used;

public class RetourProcessedEvent
{
	private String retourNo;

	private String processedAt;

	@Used("Required by jackson.")
	public RetourProcessedEvent()
	{
	}

	public RetourProcessedEvent(String retourNo, String processedAt)
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
