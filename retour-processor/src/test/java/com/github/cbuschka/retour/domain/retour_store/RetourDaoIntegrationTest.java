package com.github.cbuschka.retour.domain.retour_store;

import com.github.cbuschka.retour.util.Dates;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RetourDaoIntegrationTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private RetourDao retourDao = new RetourDao();

	@Test
	public void createsAndMarksRetour() throws RetourAlreadyProcessed
	{
		String retourNo = "R" + Dates.nowUTCAsIsoString();
		String orderNo = "O1234";

		retourDao.createRetour(retourNo, orderNo);

		retourDao.markRetourProcessed(retourNo);
	}

}
