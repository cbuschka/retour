package com.github.cbuschka.retour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.github.cbuschka.retour")
@SpringBootApplication
public class RetourProcessorApp {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(RetourProcessorApp.class, args);
	}
}
