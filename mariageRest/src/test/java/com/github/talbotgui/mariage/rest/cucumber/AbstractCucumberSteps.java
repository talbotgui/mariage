package com.github.talbotgui.mariage.rest.cucumber;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.MyDriver;

@WebIntegrationTest(randomPort = true)
// @SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
// Le deux lignes ci-dessous sont ici à cause d'un bug avec Spring boot :
// https://github.com/cucumber/cucumber-jvm/issues/783
@ContextConfiguration(classes = SeleniumTestApplication.class, loader = SpringApplicationContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractCucumberSteps {

	/** ContextRoot de l'application. */
	@Value("${server.context-path}")
	private String contextPath;

	protected MyDriver driver;

	/** Port de l'application web aléatoire injecté par Spring. */
	@Value("${local.server.port}")
	private int port;

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