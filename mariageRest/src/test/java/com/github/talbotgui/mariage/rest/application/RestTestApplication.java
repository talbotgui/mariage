package com.github.talbotgui.mariage.rest.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Une configuration Spring-boot pour l'application. Cette classe remplace le traditionnel fichier XML.
 */
@SpringBootApplication
@EntityScan({ RestApplication.ENTITY_SCAN })
@ComponentScan({ RestApplication.COMPONENT_SCAN_WEB, RestApplication.COMPONENT_SCAN_SRV })
@EnableJpaRepositories(RestApplication.JPA_REPOSITORIES)
@PropertySource(RestTestApplication.PROPERTY_SOURCE)
public class RestTestApplication {

	private static ApplicationContext ac;

	public static final String PROPERTY_SOURCE = "classpath:db-config-test.properties";

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		ac = SpringApplication.run(RestTestApplication.class);
	}

	public static void stop() {
		if (ac != null) {
			SpringApplication.exit(ac);
		}
	}

}
