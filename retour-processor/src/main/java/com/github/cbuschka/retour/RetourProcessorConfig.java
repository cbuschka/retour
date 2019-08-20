package com.github.cbuschka.retour;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = "com.github.cbuschka.retour")
@Configuration
public class RetourProcessorConfig
{
	@Bean
	public ObjectMapper objectMapper()
	{
		return new ObjectMapper();
	}
}
