package com.github.talbotgui.mariage.rest.controleur;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;

import com.github.talbotgui.mariage.rest.application.RestTestApplication;

@SpringApplicationConfiguration(classes = { RestTestApplication.class })
@WebIntegrationTest(randomPort = true)
public class BaseWebTest extends AbstractTestNGSpringContextTests {

	/** ContextRoot de l'application. */
	@Value("${server.context-path}")
	private String contextPath;

	/** Port sur lequel démarre le serveur. */
	@Value("${local.server.port}")
	private int port;

	/**
	 * Reset restTemplate avec les intercepteurs
	 *
	 * @see com.github.talbotgui.mariage.rest.controleur.ControlerTestUtil
	 *      (REST_INTERCEPTORS)
	 *
	 * @return a new RestTemplate
	 */
	protected RestTemplate getREST() {
		final RestTemplate rest = new RestTemplate();
		rest.setInterceptors(ControlerTestUtil.REST_INTERCEPTORS);
		return rest;
	}

	/** Test URL. */
	protected String getURL() {
		return "http://localhost:" + this.port + this.contextPath;
	}

}
