package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;
import com.github.talbotgui.mariage.rest.selenium.utils.SeleniumTest;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class IndexSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] { "sql/dataPurge.sql" };
	}

	@Test
	public void test01accesPage() {
		//

		//

		//
		driver.assertPageTitle("Mariage");
		driver.assertElementNotPresent(Menu.LIEN_ACCUEIL);
		driver.assertElementNotPresent(Menu.LIEN_INVITES);
		driver.assertElementPresent(Index.Button.NOUVEAU);
		driver.assertElementPresent(Index.Input.SELECTION_MARIAGE);
		driver.assertCookieNotPresentOrValid(Index.Cookie.ID_MARIAGE);
	}

	@Test
	public void test02nouveauMariageOk() {
		//
		driver.click(Index.Button.NOUVEAU, 500);
		driver.assertElementPresent(Index.Button.SAUVEGARDER);

		//
		driver.type(Index.Input.DATE_CELEBRATION, "01/01/2017", 100);
		driver.click(Index.Input.MARIE1, 0);// Pour fermer le timepicker
		driver.type(Index.Input.MARIE1, "M", 100);
		driver.type(Index.Input.MARIE2, "G", 100);
		driver.click(Index.Button.SAUVEGARDER, 500);
		driver.assertCookiePresentAndValid(Index.Cookie.ID_MARIAGE);

		//
		driver.assertElementPresent(Index.Button.MODIFIER);
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M  &  G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		driver.assertElementPresent(Index.Button.MODIFIER);
		driver.assertElementPresent(Menu.LIEN_ACCUEIL);
	}

	@Test
	public void test03modifierMariage() {
		//
		driver.click(Index.Button.MODIFIER, 200);
		driver.assertElementNotPresent(Index.Button.MODIFIER);
		driver.assertValueEquals(Index.Input.MARIE1, "M");
		driver.assertValueEquals(Index.Input.MARIE2, "G");
		driver.assertValueEquals(Index.Input.DATE_CELEBRATION, "01/01/2017");

		//
		driver.click(Index.Button.SAUVEGARDER, 500);

		//
		driver.assertElementPresent(Index.Button.MODIFIER);
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M  &  G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
	}

	@Test
	public void test04supprimerMariage() {
		//

		//
		driver.click(Index.Button.SUPPRIMER, 200);

		//
		driver.getRealDriver().get(driver.getRealDriver().getCurrentUrl());
		driver.assertCookieNotPresentOrValid(Index.Cookie.ID_MARIAGE);
		driver.assertElementNotPresent(Index.Button.MODIFIER);
		driver.assertElementNotPresent(Index.Button.SAUVEGARDER);
		driver.assertElementNotPresent(Index.Button.SUPPRIMER);
		driver.assertElementPresent(Index.Input.SELECTION_MARIAGE);
	}

}
