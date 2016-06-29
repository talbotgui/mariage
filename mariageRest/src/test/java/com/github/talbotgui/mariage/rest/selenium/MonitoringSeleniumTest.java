package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Monitoring;
import com.github.talbotgui.mariage.rest.selenium.utils.SeleniumTest;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class MonitoringSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] {};
	}

	@Test
	public void test01accesPage() throws InterruptedException {
		//

		//
		driver.get("http://localhost:" + port + contextPath + "/monitoring");

		//
		driver.assertTextEquals(Monitoring.ENTETES[0], "Instance");
		driver.assertTextEquals(Monitoring.ENTETES[1], "Label");
	}
}
