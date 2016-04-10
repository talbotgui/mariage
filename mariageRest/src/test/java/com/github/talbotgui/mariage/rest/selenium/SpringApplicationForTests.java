package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.github.talbotgui.mariage.rest.application.RestApplication;

/**
 * Une configuration Spring-boot pour le test. Cette classe remplace le traditionnel fichier XML.
 */
@SpringBootApplication
@EntityScan({ RestApplication.ENTITY_SCAN })
@ComponentScan({ RestApplication.COMPONENT_SCAN_WEB, RestApplication.COMPONENT_SCAN_SRV })
@EnableJpaRepositories(RestApplication.JPA_REPOSITORIES)
@PropertySource("classpath:db-config-test.properties")
public class SpringApplicationForTests {

}
