package com.github.cbuschka.retour.infrastructure.lambda;

import java.util.ArrayList;
import java.util.List;

public class SqsEnvelope
{
	public List<Record> Records = new ArrayList<>();

	public static class Record
	{
		public String messageId;

		public String body;
	}
}
