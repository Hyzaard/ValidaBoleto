package com.anhembi.ValidaBoleto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ValidaBoletoApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertNotNull(applicationContext, "Application context should not be null");
	}

	@Test
	void shouldLoadAllBeans() {
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		assertNotNull(beanNames, "Bean names should not be null");
		assertTrue(beanNames.length > 0, "Should have at least one bean");
	}

	@Test
	void shouldLoadConfigurationProperties() {
		assertNotNull(applicationContext.getEnvironment().getProperty("spring.application.name"), 
			"Application name should be configured");
		assertNotNull(applicationContext.getEnvironment().getProperty("spring.datasource.url"), 
			"Database URL should be configured");
	}

}
