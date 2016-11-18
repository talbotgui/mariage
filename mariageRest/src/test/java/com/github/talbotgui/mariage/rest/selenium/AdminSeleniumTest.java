package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.RestApplication;
import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Admin;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class AdminSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] { "sql/dataPurge.sql", "sql/dataSet_justeUnMariage.sql" };
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
		this.driver.select(Index.Input.SELECTION_MARIAGE, "M & G", 100);
		this.driver.click(Menu.LIEN_ADMINISTRATION, 1000);

		//
		this.driver.assertPageTitle("Mariage");
		this.driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		this.driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		this.driver.assertTextEquals(Admin.CASES[0][0], RestApplication.LOGIN_MDP_ADMIN_PAR_DEFAUT);
		this.driver.assertElementPresent(Admin.Button.AFFICHE_POPUP);
	}

	@Test
	public void test02ajoutKo() {

		//
		this.driver.click(Admin.Button.AFFICHE_POPUP, 500);
		this.driver.assertElementPresent(Admin.Button.AJOUT);
		this.driver.assertElementPresent(Admin.Button.AFFICHE_POPUP);

		//
		this.driver.click(Admin.Button.AJOUT, 500);

		//
		this.driver.assertNumberOfElements(Commun.DIV_ERREUR, 2);
	}

	@Test
	public void test03ajoutOk() {
		//

		//
		this.driver.type(Admin.Input.LOGIN, "monLogin", 200);
		this.driver.type(Admin.Input.MDP, "monMdp", 200);
		this.driver.click(Admin.Button.AJOUT, 500);

		//
		this.driver.assertNumberOfElements(Commun.DIV_ERREUR, 0);
		this.driver.assertTextEquals(Admin.CASES[0][0], RestApplication.LOGIN_MDP_ADMIN_PAR_DEFAUT);
		this.driver.assertTextEquals(Admin.CASES[1][0], "monLogin");
	}

	@Test
	public void test04SupprimeOk() {
		//
		this.driver.click(Admin.Button.AFFICHE_POPUP, 500);
		this.driver.type(Admin.Input.LOGIN, "vaEtreSupprimer", 200);
		this.driver.type(Admin.Input.MDP, "vaEtreSupprimer", 200);
		this.driver.click(Admin.Button.AJOUT, 500);
		this.driver.assertTextEquals(Admin.CASES[2][0], "vaEtreSupprimer");

		//
		this.driver.click(Admin.Button.SUPPRIMER[2], 500);

		//
		this.driver.assertTextEquals(Admin.CASES[1][0], "monLogin");
		this.driver.assertElementNotPresent(Admin.CASES[2][0]);
	}

	@Test
	@Override
	public void test99logout() {
		super.test99logout();
	}
}
