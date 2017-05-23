package com.github.talbotgui.mariage.rest.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Une configuration Spring-boot pour l'application. Cette classe remplace le
 * traditionnel fichier XML.
 *
 * Cette configuration est différente de RestApplication sur :
 * <ul>
 * <li>Pas de Swagger actif</li>
 * <li>Pas de filtre de sécurité actif</li>
 * <li>Le propertySource pointe sur un fichier de configuration dédié aux
 * tests</li>
 * </ul>
 */
@SpringBootApplication
@EntityScan({ RestApplication.ENTITY_SCAN })
@ComponentScan({ RestApplication.COMPONENT_SCAN_WEB, RestApplication.COMPONENT_SCAN_SRV,
		RestApplication.JPA_REPOSITORIES })
@EnableJpaRepositories(RestApplication.JPA_REPOSITORIES)
@PropertySource(RestTestApplication.PROPERTY_SOURCE)
public class RestTestApplication {

	public static final String PROPERTY_SOURCE = "classpath:db-config-test.properties";

}
