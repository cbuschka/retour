package com.github.cbuschka.retour.infrastructure.lambda;

import com.amazonaws.services.lambda.runtime.Context;

public class ThreadLocalContext
{
	private static final ThreadLocal<Context> threadLocalContext = new ThreadLocal<>();

	public static Context get()
	{
		return threadLocalContext.get();
	}

	public static void runWith(Runnable r, Context context)
	{
		try
		{
			threadLocalContext.set(context);
			r.run();
		}
		finally
		{
			threadLocalContext.remove();
		}
	}
}
