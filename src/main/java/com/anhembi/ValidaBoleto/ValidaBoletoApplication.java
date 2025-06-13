package com.anhembi.ValidaBoleto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.anhembi.ValidaBoleto")
public class ValidaBoletoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidaBoletoApplication.class, args);
	}

}
