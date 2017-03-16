package com.github.talbotgui.mariage.rest.cucumber;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;

import com.github.talbotgui.mariage.metier.service.SecuriteService;
import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.MyDriver;

@ContextConfiguration
@SpringBootTest(classes = SeleniumTestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbstractCucumberSteps {

	/** ContextRoot de l'application. */
	@Value("${server.context-path}")
	private String contextPath;

	protected MyDriver driver;

	/** Port de l'application web aléatoire injecté par Spring. */
	@Value("${local.server.port}")
	private int port;

	@Autowired
	protected SecuriteService securiteService;

	public AbstractCucumberSteps() {
		super();
	}

	@PreDestroy
	public void afterScenario() throws Exception {
		this.driver.quit();
	}

	@PostConstruct
	public void beforeScenario() {
		this.driver = new MyDriver(this.port, this.contextPath);
	}

}