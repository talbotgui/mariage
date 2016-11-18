package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class IndexSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] { "sql/dataPurge.sql" };
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

		//
		this.driver.assertPageTitle("Mariage");
		this.driver.assertElementNotPresent(Menu.LIEN_ACCUEIL);
		this.driver.assertElementNotPresent(Menu.LIEN_INVITES);
		this.driver.assertElementPresent(Index.Button.NOUVEAU);
		this.driver.assertElementPresent(Index.Input.SELECTION_MARIAGE);
		this.driver.assertCookieNotPresentOrValid(Index.Cookie.ID_MARIAGE);
	}

	@Test
	public void test02nouveauMariageOk() {
		//
		this.driver.click(Index.Button.NOUVEAU, 500);
		this.driver.assertElementPresent(Index.Button.SAUVEGARDER);

		//
		this.driver.type(Index.Input.DATE_CELEBRATION, "01/01/2017", 100);
		this.driver.click(Index.Input.MARIE1, 0);// Pour fermer le timepicker
		this.driver.type(Index.Input.MARIE1, "M", 100);
		this.driver.type(Index.Input.MARIE2, "G", 100);
		this.driver.click(Index.Button.SAUVEGARDER, 500);
		this.driver.assertCookiePresentAndValid(Index.Cookie.ID_MARIAGE);

		//
		this.driver.assertElementPresent(Index.Button.MODIFIER);
		this.driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M  &  G");
		this.driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		this.driver.assertElementPresent(Index.Button.MODIFIER);
		this.driver.assertElementPresent(Menu.LIEN_ACCUEIL);
	}

	@Test
	public void test03modifierMariage() {
		//
		this.driver.click(Index.Button.MODIFIER, 200);
		this.driver.assertElementNotPresent(Index.Button.MODIFIER);
		this.driver.assertValueEquals(Index.Input.MARIE1, "M");
		this.driver.assertValueEquals(Index.Input.MARIE2, "G");
		this.driver.assertValueEquals(Index.Input.DATE_CELEBRATION, "01/01/2017");

		//
		this.driver.click(Index.Button.SAUVEGARDER, 500);

		//
		this.driver.assertElementPresent(Index.Button.MODIFIER);
		this.driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M  &  G");
		this.driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
	}

	@Test
	public void test04supprimerMariage() {
		//

		//
		this.driver.click(Index.Button.SUPPRIMER, 200);

		//
		this.driver.getRealDriver().get(this.driver.getRealDriver().getCurrentUrl());
		this.driver.assertCookieNotPresentOrValid(Index.Cookie.ID_MARIAGE);
		this.driver.assertElementNotPresent(Index.Button.MODIFIER);
		this.driver.assertElementNotPresent(Index.Button.SAUVEGARDER);
		this.driver.assertElementNotPresent(Index.Button.SUPPRIMER);
		this.driver.assertElementPresent(Index.Input.SELECTION_MARIAGE);
	}

	@Test
	@Override
	public void test99logout() {
		super.test99logout();
	}

}
