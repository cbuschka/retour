package com.github.cbuschka.retour.business.retour_processor;

import com.github.codestickers.Used;

import javax.validation.constraints.NotBlank;

public class RetourMessage
{
	@NotBlank
	private String retourNo;

	@NotBlank
	private String orderNo;

	@Used("required for deserialization")
	public RetourMessage()
	{
	}

	public RetourMessage(String retourNo, String orderNo)
	{
		this.retourNo = retourNo;
		this.orderNo = orderNo;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public void setRetourNo(String retourNo)
	{
		this.retourNo = retourNo;
	}

	public String getRetourNo()
	{
		return retourNo;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{" +
				"retourNo='" + retourNo + '\'' +
				"orderNo='" + retourNo + '\'' +
				'}';
	}
}
