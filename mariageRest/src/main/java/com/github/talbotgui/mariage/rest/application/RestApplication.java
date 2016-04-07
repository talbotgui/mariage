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
@PropertySource(RestApplication.PROPERTY_SOURCE)
public class RestApplication {

	private static ApplicationContext ac;

	protected static final String COMPONENT_SCAN_SRV = "com.github.talbotgui.mariage.metier.service";
	protected static final String COMPONENT_SCAN_WEB = "com.github.talbotgui.mariage.rest";
	protected static final String ENTITY_SCAN = "com.github.talbotgui.mariage.metier.entities";
	protected static final String JPA_REPOSITORIES = "com.github.talbotgui.mariage.metier.dao";
	protected static final String PROPERTY_SOURCE = "classpath:db-config.properties";

	public static ApplicationContext getApplicationContext() {
		return ac;
	}

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		ac = SpringApplication.run(RestApplication.class);
	}

	public static void stop() {
		if (ac != null) {
			SpringApplication.exit(ac);
		}
	}

}
