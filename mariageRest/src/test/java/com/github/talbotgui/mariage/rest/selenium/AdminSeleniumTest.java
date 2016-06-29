package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Admin;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;
import com.github.talbotgui.mariage.rest.selenium.utils.SeleniumTest;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class AdminSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] { "sql/dataPurge.sql", "sql/dataSet_justeUnMariage.sql" };
	}

	@Test
	public void test01accesPage() throws InterruptedException {
		//

		//
		driver.select(Index.Input.SELECTION_MARIAGE, "M & G", 100);
		driver.click(Menu.LIEN_ADMINISTRATION, 1000);

		//
		driver.assertPageTitle("Mariage");
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		driver.assertTextEquals(Commun.TABLEAU_MESSAGE_VIDE, "No data to display");
		driver.assertElementPresent(Admin.Button.AFFICHE_POPUP);
	}

	@Test
	public void test02ajoutKo() throws InterruptedException {

		//
		driver.click(Admin.Button.AFFICHE_POPUP, 500);
		driver.assertElementPresent(Admin.Button.AJOUT);
		driver.assertElementPresent(Admin.Button.AFFICHE_POPUP);

		//
		driver.click(Admin.Button.AJOUT, 500);

		//
		driver.count(Commun.DIV_ERREUR, 2);
	}

	@Test
	public void test03ajoutOk() throws InterruptedException {
		//

		//
		driver.type(Admin.Input.LOGIN, "monLogin", 200);
		driver.type(Admin.Input.MDP, "monMdp", 200);
		driver.click(Admin.Button.AJOUT, 500);

		//
		driver.assertTextEquals(Admin.CASES[0][0], "monLogin");
	}

}
