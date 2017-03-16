package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.ParametresEtape;

@SpringBootTest(classes = SeleniumTestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ParametresEtapeSeleniumTest extends SeleniumTest {

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
	public void test01Acces() {
		//

		//
		this.driver.select(Index.Input.SELECTION_MARIAGE, "M & G", 100);
		this.driver.click(Menu.LIEN_PARAMETAPES, 500);

		//
		this.driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		this.driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");

		this.driver.assertTextEquals(ParametresEtape.ENTETES[0], "Type");
		this.driver.assertTextEquals(ParametresEtape.ENTETES[1], "Ordre");
		this.driver.assertTextEquals(ParametresEtape.ENTETES[2], "Nom");
		this.driver.assertTextEquals(ParametresEtape.ENTETES[3], "Date et heure");
		this.driver.assertTextEquals(ParametresEtape.ENTETES[4], "Lieu");
		this.driver.assertTextEquals(ParametresEtape.ENTETES[5], "Actions");

		this.driver.assertTextEquals(Commun.TABLEAU_MESSAGE_VIDE, "No data to display");
	}

	@Test
	public void test02ValidationFormulaireRepas() {
		//

		//
		this.driver.click(ParametresEtape.Button.AFFICHE_POPUP_REPAS, 500);
		this.driver.type(ParametresEtape.Input.REPA_NOM, "", 15);
		this.driver.type(ParametresEtape.Input.REPA_LIEU, "", 15);
		this.driver.type(ParametresEtape.Input.REPA_DATEHEURE, "", 15);
		this.driver.click(ParametresEtape.Button.AJOUT, 200);

		//
		this.driver.assertNumberOfElements(Commun.DIV_ERREUR, 3);
	}

	@Test
	public void test03AjouterEtapeRepas() {
		//

		//
		this.driver.click(ParametresEtape.Button.AFFICHE_POPUP_REPAS, 500);
		this.driver.type(ParametresEtape.Input.REPA_NOM, "Repas1", 15);
		this.driver.type(ParametresEtape.Input.REPA_LIEU, "LieuRepas1", 15);
		this.driver.type(ParametresEtape.Input.REPA_DATEHEURE, "01/01/2017 12:00", 15);
		this.driver.click(ParametresEtape.Button.AJOUT, 200);

		//
		this.driver.assertTextEquals(ParametresEtape.CASES[0][0], "EtapeRepas");
		this.driver.assertTextEquals(ParametresEtape.CASES[0][1], "1");
		this.driver.assertTextEquals(ParametresEtape.CASES[0][2], "Repas1");
		this.driver.assertTextEquals(ParametresEtape.CASES[0][3], "01/01/2017 12:00");
		this.driver.assertTextEquals(ParametresEtape.CASES[0][4], "LieuRepas1");
	}

	@Test
	public void test04ValidationFormulaireCeremonie() {
		//

		//
		this.driver.click(ParametresEtape.Button.AFFICHE_POPUP_CEREMONIE, 500);
		this.driver.type(ParametresEtape.Input.CERE_NOM, "", 15);
		this.driver.type(ParametresEtape.Input.CERE_LIEU, "", 15);
		this.driver.type(ParametresEtape.Input.CERE_DATEHEURE, "", 15);
		this.driver.type(ParametresEtape.Input.CERE_CELEBRANT, "", 15);
		this.driver.click(ParametresEtape.Button.CERE_AJOUT, 200);

		//
		this.driver.assertNumberOfElements(Commun.DIV_ERREUR, 4);
	}

	@Test
	public void test05AjouterEtapeCeremonie() {
		//

		//
		this.driver.click(ParametresEtape.Button.AFFICHE_POPUP_CEREMONIE, 500);
		this.driver.type(ParametresEtape.Input.CERE_NOM, "Ceremonie1", 15);
		this.driver.type(ParametresEtape.Input.CERE_LIEU, "LieuCeremonie1", 15);
		this.driver.type(ParametresEtape.Input.CERE_DATEHEURE, "01/01/2017 13:00", 15);
		this.driver.type(ParametresEtape.Input.CERE_CELEBRANT, "Celebrant1", 15);
		this.driver.click(ParametresEtape.Button.CERE_AJOUT, 200);

		//
		this.driver.assertTextEquals(ParametresEtape.CASES[1][0], "EtapeCeremonie");
		this.driver.assertTextEquals(ParametresEtape.CASES[1][1], "2");
		this.driver.assertTextEquals(ParametresEtape.CASES[1][2], "Ceremonie1");
		this.driver.assertTextEquals(ParametresEtape.CASES[1][3], "01/01/2017 13:00");
		this.driver.assertTextEquals(ParametresEtape.CASES[1][4], "LieuCeremonie1");
	}

	@Test
	@Override
	public void test99logout() {
		super.test99logout();
	}

}
