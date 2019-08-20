package com.github.cbuschka.retour.business.retour_processor;

import com.github.cbuschka.retour.AwsConfig;
import com.github.cbuschka.retour.RetourProcessorConfig;
import com.github.cbuschka.retour.infrastructure.spring.ApplicationContextFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SpringContextIntegrationTest
{
	private AnnotationConfigApplicationContext applicationContext;

	@Before
	public void before()
	{
		this.applicationContext = ApplicationContextFactory.newApplicationContext(RetourProcessorConfig.class, AwsConfig.class);
	}

	@After
	public void after()
	{
		this.applicationContext.close();
	}

	@Test
	public void contextLoads()
	{
		RetourProcessor bean = this.applicationContext.getBean(RetourProcessor.class);

		assertThat(bean, is(not(nullValue())));
	}
}
