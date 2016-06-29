package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.ParametresEtape;
import com.github.talbotgui.mariage.rest.selenium.utils.SeleniumTest;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class ParametresEtapeSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] { "sql/dataPurge.sql", "sql/dataSet_justeUnMariage.sql" };
	}

	@Test
	public void test01Acces() {
		//

		//
		driver.select(Index.Input.SELECTION_MARIAGE, "M & G", 100);
		driver.click(Menu.LIEN_PARAMETAPES, 500);

		//
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");

		driver.assertTextEquals(ParametresEtape.ENTETES[0], "Type");
		driver.assertTextEquals(ParametresEtape.ENTETES[1], "Ordre");
		driver.assertTextEquals(ParametresEtape.ENTETES[2], "Nom");
		driver.assertTextEquals(ParametresEtape.ENTETES[3], "Date et heure");
		driver.assertTextEquals(ParametresEtape.ENTETES[4], "Lieu");
		driver.assertTextEquals(ParametresEtape.ENTETES[5], "Actions");

		driver.assertTextEquals(Commun.TABLEAU_MESSAGE_VIDE, "No data to display");
	}

	@Test
	public void test02ValidationFormulaireRepas() {
		//

		//
		driver.click(ParametresEtape.Button.AFFICHE_POPUP_REPAS, 500);
		driver.type(ParametresEtape.Input.REPA_NOM, "", 15);
		driver.type(ParametresEtape.Input.REPA_LIEU, "", 15);
		driver.type(ParametresEtape.Input.REPA_DATEHEURE, "", 15);
		driver.click(ParametresEtape.Button.AJOUT, 200);

		//
		driver.count(Commun.DIV_ERREUR, 3);
	}

	@Test
	public void test03AjouterEtapeRepas() {
		//

		//
		driver.click(ParametresEtape.Button.AFFICHE_POPUP_REPAS, 500);
		driver.type(ParametresEtape.Input.REPA_NOM, "Repas1", 15);
		driver.type(ParametresEtape.Input.REPA_LIEU, "LieuRepas1", 15);
		driver.type(ParametresEtape.Input.REPA_DATEHEURE, "01/01/2017 12:00", 15);
		driver.click(ParametresEtape.Button.AJOUT, 200);

		//
		driver.assertTextEquals(ParametresEtape.CASES[0][0], "EtapeRepas");
		driver.assertTextEquals(ParametresEtape.CASES[0][1], "1");
		driver.assertTextEquals(ParametresEtape.CASES[0][2], "Repas1");
		driver.assertTextEquals(ParametresEtape.CASES[0][3], "01/01/2017 12:00");
		driver.assertTextEquals(ParametresEtape.CASES[0][4], "LieuRepas1");
	}

	@Test
	public void test04ValidationFormulaireCeremonie() {
		//

		//
		driver.click(ParametresEtape.Button.AFFICHE_POPUP_CEREMONIE, 500);
		driver.type(ParametresEtape.Input.CERE_NOM, "", 15);
		driver.type(ParametresEtape.Input.CERE_LIEU, "", 15);
		driver.type(ParametresEtape.Input.CERE_DATEHEURE, "", 15);
		driver.type(ParametresEtape.Input.CERE_CELEBRANT, "", 15);
		driver.click(ParametresEtape.Button.CERE_AJOUT, 200);

		//
		driver.count(Commun.DIV_ERREUR, 4);
	}

	@Test
	public void test05AjouterEtapeCeremonie() {
		//

		//
		driver.click(ParametresEtape.Button.AFFICHE_POPUP_CEREMONIE, 500);
		driver.type(ParametresEtape.Input.CERE_NOM, "Ceremonie1", 15);
		driver.type(ParametresEtape.Input.CERE_LIEU, "LieuCeremonie1", 15);
		driver.type(ParametresEtape.Input.CERE_DATEHEURE, "01/01/2017 13:00", 15);
		driver.type(ParametresEtape.Input.CERE_CELEBRANT, "Celebrant1", 15);
		driver.click(ParametresEtape.Button.CERE_AJOUT, 200);

		//
		driver.assertTextEquals(ParametresEtape.CASES[1][0], "EtapeCeremonie");
		driver.assertTextEquals(ParametresEtape.CASES[1][1], "2");
		driver.assertTextEquals(ParametresEtape.CASES[1][2], "Ceremonie1");
		driver.assertTextEquals(ParametresEtape.CASES[1][3], "01/01/2017 13:00");
		driver.assertTextEquals(ParametresEtape.CASES[1][4], "LieuCeremonie1");
	}

}
