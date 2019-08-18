package com.github.cbuschka.retour.util;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Logger
{
	private static final ThreadLocal<LambdaLogger> threadLocalLambdaLogger = new InheritableThreadLocal<>();

	private static Logger instance = new Logger();

	public static Logger get()
	{
		return instance;
	}

	public void run(Runnable r, LambdaLogger lambdaLogger)
	{
		try
		{
			threadLocalLambdaLogger.set(lambdaLogger);
			r.run();
		}
		finally
		{
			threadLocalLambdaLogger.remove();
		}
	}

	public void log(String message)
	{
		LambdaLogger lambdaLogger = threadLocalLambdaLogger.get();
		if (lambdaLogger != null)
		{
			lambdaLogger.log(message);
		}
		else
		{
			System.err.println(message);
		}
	}
}
