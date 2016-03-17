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
@EntityScan({ "com.github.talbotgui.mariage.metier.entities" })
@ComponentScan({ "com.github.talbotgui.mariage.rest.controleur", "com.github.talbotgui.mariage.metier.service" })
@EnableJpaRepositories("com.github.talbotgui.mariage.metier.dao")
@PropertySource("classpath:db-config.properties")
public class RestApplication {

	private static ApplicationContext ac;

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
