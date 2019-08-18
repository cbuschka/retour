package com.github.cbuschka.retour.util;

import com.amazonaws.services.lambda.runtime.Context;
import com.github.cbuschka.retour.lambda.ThreadLocalContext;

public class Logger
{
	private static Logger instance = new Logger();

	public static Logger get()
	{
		return instance;
	}

	public void log(String message)
	{
		Context context = ThreadLocalContext.get();
		if (context != null)
		{
			context.getLogger().log(context.getAwsRequestId() + ": "+ message);
		}
		else
		{
			System.err.println(message);
		}
	}
}
