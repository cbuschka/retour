package com.github.cbuschka.retour.retour_processor;

import com.github.codestickers.Used;
import javax.validation.constraints.NotBlank;

public class ProcessRetourMessage {

	@NotBlank
	private String retourNo;

	@Used("required for deserialization")
	public ProcessRetourMessage() {
	}

	public ProcessRetourMessage(String retourNo) {
		this.retourNo = retourNo;
	}

	public void setRetourNo(String retourNo) {
		this.retourNo = retourNo;
	}

	public String getRetourNo() {
		return retourNo;
	}
}
