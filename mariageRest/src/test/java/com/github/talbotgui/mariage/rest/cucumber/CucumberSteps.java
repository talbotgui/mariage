package com.github.talbotgui.mariage.rest.cucumber;

import java.util.Date;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Login;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CucumberSteps extends AbstractCucumberSteps {

	@Given("^connexion en tant que ([A-Z]*)$")
	public void connexion(final String role) throws Throwable {
		// Creation d'un user avec le role demandé
		final String login = "login" + (new Date()).getTime();
		final String mdp = "123456";
		this.securiteService.sauvegarderUtilisateur(login, mdp, Role.valueOf(role));

		// Loggin
		this.driver.type(Login.Input.LOGIN, login, 200);
		this.driver.type(Login.Input.MDP, mdp, 200);
		this.driver.click(Login.Button.LOGIN, 800);

		// Assert
		this.driver.assertCookiePresentAndValid(Login.Cookie.JSESSIONID);
		this.driver.assertPageTitle("Mariage");

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
