package com.github.talbotgui.mariage.rest.selenium;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.MyDriver;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Monitoring;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class MonitoringSeleniumTest extends AbstractTestNGSpringContextTests {

	/** ContextRoot de l'application. */
	@Value("${server.context-path}")
	private String contextPath;

	private MyDriver driver;

	/** Port de l'application web aléatoire injecté par Spring. */
	@Value("${local.server.port}")
	private int port;

	@AfterClass
	public void afterClass() throws Exception {
		driver.quit();
	}

	@BeforeClass
	public void beforeClass() {
		driver = new MyDriver(new HtmlUnitDriver(BrowserVersion.FIREFOX_38, true));
	}

	@Test
	public void test01accesPage() throws InterruptedException {
		//
		driver.deleteAllCookies();

		//
		driver.get("http://localhost:" + port + contextPath + "/monitoring");

		//
		driver.assertTextEquals(Monitoring.ENTETES[0], "Instance");
		driver.assertTextEquals(Monitoring.ENTETES[1], "Label");
	}
}
