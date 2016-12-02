package com.github.talbotgui.mariage.rest.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Une configuration Spring-boot pour le test. Cette classe remplace le
 * traditionnel fichier XML.
 *
 * Cette configuration est différente de RestApplication sur :
 * <ul>
 * <li>Pas de Swagger actif</li>
 * <li>Le propertySource pointe sur un fichier de configuration dédié aux
 * tests</li>
 * </ul>
 */
@SpringBootApplication
@EntityScan({ RestApplication.ENTITY_SCAN })
@ComponentScan({ RestApplication.COMPONENT_SCAN_WEB, RestApplication.COMPONENT_SCAN_SECU,
		RestApplication.COMPONENT_SCAN_SRV })
@EnableJpaRepositories(RestApplication.JPA_REPOSITORIES)
@PropertySource(RestTestApplication.PROPERTY_SOURCE)
public class SeleniumTestApplication {
}
