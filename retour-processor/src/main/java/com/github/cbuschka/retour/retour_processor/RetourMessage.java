package com.github.cbuschka.retour.retour_processor;

import com.github.codestickers.Used;
import javax.validation.constraints.NotBlank;

public class RetourMessage
{

	@NotBlank
	private String retourNo;

	@Used("required for deserialization")
	public RetourMessage() {
	}

	public RetourMessage(String retourNo) {
		this.retourNo = retourNo;
	}

	public void setRetourNo(String retourNo) {
		this.retourNo = retourNo;
	}

	public String getRetourNo() {
		return retourNo;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{" +
				"retourNo='" + retourNo + '\'' +
				'}';
	}
}
