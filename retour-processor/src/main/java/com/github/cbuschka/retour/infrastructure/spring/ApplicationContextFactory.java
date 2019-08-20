package com.github.cbuschka.retour.infrastructure.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextFactory
{
	public static AnnotationConfigApplicationContext newApplicationContext(Class<?> ...annotatedClasses)
	{
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(annotatedClasses);
		return applicationContext;
	}
}
