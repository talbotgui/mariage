package com.github.talbotgui.mariage.rest.cucumber;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.MyDriver;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@WebIntegrationTest(randomPort = true)
// @SpringApplicationConfiguration(classes = SeleniumTestApplication.class)

// Le deux lignes ci-dessous sont ici à cause d'un bug :
// https://github.com/cucumber/cucumber-jvm/issues/783
@ContextConfiguration(classes = SeleniumTestApplication.class, loader = SpringApplicationContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CucumberSteps {

	/** ContextRoot de l'application. */
	@Value("${server.context-path}")
	private String contextPath;

	protected MyDriver driver;

	/** Port de l'application web aléatoire injecté par Spring. */
	@Value("${local.server.port}")
	private int port;

	@After
	public void afterScenario() throws Exception {
		this.driver.quit();
	}

	@Before
	public void beforeScenario() {
		if (this.driver == null) {
			this.driver = new MyDriver(this.port, this.contextPath);
		}
	}

	@When("^cree mariage \"([^\"]*)\" et \"([^\"]*)\" le \"([^\"]*)\"$")
	public void creeMariage(final String arg1, final String arg2, final String arg3) throws Throwable {
		this.driver.get(Selectors.Index.NOM_PAGE);
		this.driver.click(Selectors.Index.Button.NOUVEAU, 200);
		this.driver.type(Selectors.Index.Input.MARIE1, arg1, 100);
		this.driver.type(Selectors.Index.Input.MARIE2, arg2, 100);
		this.driver.type(Selectors.Index.Input.DATE_CELEBRATION, arg3, 100);
		this.driver.click(Selectors.Index.Button.SAUVEGARDER, 200);
		this.driver.assertElementPresent(Selectors.Index.Button.MODIFIER);
	}

	@Then("^mariage \"([^\"]*)\" et \"([^\"]*)\" selectionne$")
	public void mariageSelectionne(final String arg1, final String arg2) throws Throwable {
		this.driver.get(Selectors.Index.NOM_PAGE);
		this.driver.assertTextEquals(Selectors.Commun.DIV_MARIES, "Mariage de " + arg1 + "  &  " + arg2 + "");
	}

	@Then("^utilisateur sur page index$")
	public void utilisateurSurPageIndex() throws Throwable {
		this.driver.get(Selectors.Index.NOM_PAGE);
	}
}
