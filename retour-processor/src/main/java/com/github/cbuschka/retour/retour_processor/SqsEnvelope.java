package com.github.cbuschka.retour.retour_processor;

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
