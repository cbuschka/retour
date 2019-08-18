package com.github.cbuschka.retour.domain.refund_buyer;

public class RefundBuyerMessage {

	public String retourNo;

	public RefundBuyerMessage(String retourNo)
	{
		this.retourNo = retourNo;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"retourNo='" + retourNo + '\'' +
				'}';
	}
}
