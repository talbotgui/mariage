package com.github.talbotgui.mariage.metier.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Une configuration Spring-boot pour le test. Cette classe remplace le
 * traditionnel fichier XML.
 */
@SpringBootApplication
@EntityScan("com.github.talbotgui.mariage.metier.entities")
@ComponentScan({ "com.github.talbotgui.mariage.metier.service" })
@EnableJpaRepositories("com.github.talbotgui.mariage.metier.dao")
@PropertySource("classpath:db-config.properties")
public class SpringApplicationForTests {

}
