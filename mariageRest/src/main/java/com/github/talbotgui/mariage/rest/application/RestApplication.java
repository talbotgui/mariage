package com.github.talbotgui.mariage.rest.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.github.talbotgui.mariage.metier.service.SecuriteService;

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

	public static final String COMPONENT_SCAN_SRV = "com.github.talbotgui.mariage.metier.service";
	public static final String COMPONENT_SCAN_WEB = "com.github.talbotgui.mariage.rest.controleur";
	public static final String ENTITY_SCAN = "com.github.talbotgui.mariage.metier.entities";
	public static final String JPA_REPOSITORIES = "com.github.talbotgui.mariage.metier.dao";
	public static final String PROPERTY_SOURCE = "classpath:db-config-prod.properties";

	public static ApplicationContext getApplicationContext() {
		return ac;
	}

	public static void main(final String[] args) {
		start();
	}

	public static void start() {
		ac = SpringApplication.run(RestApplication.class);

		// Si aucun utilisateur au base, on en crée un par défaut
		final SecuriteService securiteService = ac.getBean(SecuriteService.class);
		if (securiteService.listeUtilisateurs().isEmpty()) {
			securiteService.creeUtilisateur("adminAsupprimer", "adminAsupprimer");
		}
	}

	public static void stop() {
		if (ac != null) {
			SpringApplication.exit(ac);
		}
	}

}
