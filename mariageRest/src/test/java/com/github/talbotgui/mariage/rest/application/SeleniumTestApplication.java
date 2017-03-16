package com.github.talbotgui.mariage.rest.application;

import java.time.LocalDate;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Une configuration Spring-boot pour le test. Cette classe remplace le
 * traditionnel fichier XML.
 *
 * Cette configuration est différente de RestApplication sur :
 * <ul>
 * <li>Le propertySource pointe sur un fichier de configuration dédié aux
 * tests</li>
 * </ul>
 */
@SpringBootApplication
@EnableSwagger2
@EntityScan({ RestApplication.ENTITY_SCAN })
@ComponentScan({ RestApplication.COMPONENT_SCAN_WEB, RestApplication.COMPONENT_SCAN_SECU,
		RestApplication.COMPONENT_SCAN_SRV })
@EnableJpaRepositories(RestApplication.JPA_REPOSITORIES)
@PropertySource(RestTestApplication.PROPERTY_SOURCE)
public class SeleniumTestApplication {

	/**
	 * Bean fo SpringFox.
	 *
	 * @return SpringFox configuration
	 */
	@Bean
	public Docket getRestApiDescriptionWithSwagger() {

		// see
		// http://springfox.github.io/springfox/docs/current/#springfox-samples
		return new Docket(DocumentationType.SWAGGER_2)//
				.select().apis(RequestHandlerSelectors.any())//
				.paths(PathSelectors.any()).build().pathMapping("/")//
				.directModelSubstitute(LocalDate.class, String.class)//
				.genericModelSubstitutes(ResponseEntity.class)//
				.enableUrlTemplating(true)//
				.tags(new Tag("API MonMariage", "Description de l'API de l'application MonMariage"));
	}
}
