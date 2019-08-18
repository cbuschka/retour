package com.github.cbuschka.retour.domain.charge_seller;

public class ChargeSellerMessage
{
	public String retourNo;

	public ChargeSellerMessage(String retourNo)
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
