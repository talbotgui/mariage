package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Monitoring;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class MonitoringSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] {};
	}

	@Test
	@Override
	public void test00login() {
		super.test00login();
	}

	@Test
	public void test01accesPage() {
		//

		//
		this.driver.get("/monitoring");

		//
		this.driver.assertTextEquals(Monitoring.ENTETES[0], "Instance");
		this.driver.assertTextEquals(Monitoring.ENTETES[1], "Label");
	}

}
