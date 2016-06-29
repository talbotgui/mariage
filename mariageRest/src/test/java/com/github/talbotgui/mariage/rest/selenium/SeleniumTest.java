package com.github.talbotgui.mariage.rest.selenium;

import org.openqa.selenium.By;
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
import com.github.talbotgui.mariage.rest.selenium.Selectors.Admin;
import com.github.talbotgui.mariage.rest.selenium.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.Selectors.Invite;
import com.github.talbotgui.mariage.rest.selenium.Selectors.Invite.Input;
import com.github.talbotgui.mariage.rest.selenium.Selectors.Menu;
import com.github.talbotgui.mariage.rest.selenium.Selectors.ParametresEtape;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class SeleniumTest extends AbstractTestNGSpringContextTests {

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
	public void test01Index01accesPage() throws InterruptedException {
		//
		driver.deleteAllCookies();

		//
		driver.get("http://localhost:" + port + contextPath + "/");

		//
		driver.assertPageTitle("Mariage");
		driver.assertElementNotPresent(Menu.LIEN_ACCUEIL);
		driver.assertElementNotPresent(Menu.LIEN_INVITATION);
		driver.assertElementPresent(Index.Button.NOUVEAU);
		driver.assertElementPresent(Index.Input.SELECTION_MARIAGE);
	}

	@Test
	public void test01Index01nouveauMariage() throws InterruptedException {
		//
		driver.click(Index.Button.NOUVEAU, 500);
		driver.assertElementPresent(Index.Button.SAUVEGARDER);

		//
		driver.type(Index.Input.DATE_CELEBRATION, "01/01/2017", 100);
		driver.click(Index.Input.MARIE1, 0);// Pour fermer le timepicker
		driver.type(Index.Input.MARIE1, "M", 100);
		driver.type(Index.Input.MARIE2, "G", 100);
		driver.click(Index.Button.SAUVEGARDER, 500);

		//
		driver.assertElementPresent(Index.Button.MODIFIER);
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M  &  G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		driver.assertElementPresent(Index.Button.MODIFIER);
		driver.assertElementPresent(Menu.LIEN_ACCUEIL);
	}

	@Test
	public void test01Index02modifierMariage() throws InterruptedException {
		//
		driver.click(Index.Button.MODIFIER, 200);

		//
		driver.assertValueEquals(Index.Input.MARIE1, "M");
		driver.assertValueEquals(Index.Input.MARIE2, "G");
		driver.assertValueEquals(Index.Input.DATE_CELEBRATION, "01/01/2017");
		driver.click(Index.Button.SAUVEGARDER, 500);

		//
		driver.assertElementPresent(Index.Button.MODIFIER);
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M  &  G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
	}

	@Test
	public void test02ParametresEtape01Acces() throws InterruptedException {
		//

		//
		driver.click(Menu.LIEN_PARAMETAPES, 200);

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
	public void test02ParametresEtape02ValidationFormulaireRepas() throws InterruptedException {
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
	public void test02ParametresEtape03AjouterEtapeRepas() throws InterruptedException {
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
	public void test02ParametresEtape04ValidationFormulaireCeremonie() throws InterruptedException {
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
	public void test02ParametresEtape05AjouterEtapeCeremonie() throws InterruptedException {
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

	@Test
	public void test03Invite01accesPage() throws InterruptedException {
		//

		//
		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertPageTitle("Mariage");
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		driver.assertTextEquals(Commun.TABLEAU_MESSAGE_VIDE, "No data to display");
		driver.assertElementPresent(Invite.Button.AFFICHE_POPUP_SAISIE);
	}

	@Test
	public void test03Invite02ajoutKo() throws InterruptedException {

		//
		driver.click(Invite.Button.AFFICHE_POPUP_SAISIE, 500);
		driver.assertElementPresent(Invite.Button.AJOUTER);
		driver.assertElementPresent(Invite.DIV_POPUP);

		//
		driver.click(Invite.Button.AJOUTER, 500);

		//
		driver.count(Commun.DIV_ERREUR, 4);
	}

	@Test
	public void test03Invite03ajoutOk() throws InterruptedException {
		//

		//
		driver.type(Invite.Input.GROUPE, "groupe1", 200);
		driver.type(Invite.Input.FOYER, "foyer1", 200);
		driver.type(Invite.Input.NOM, "nom1", 200);
		driver.type(Invite.Input.PRENOM, "prenom1", 200);
		driver.click(Invite.Button.AJOUTER, 500);

		//
		driver.assertTextEquals(Invite.CASES[0][0], "groupe1");
		driver.assertTextEquals(Invite.CASES[0][1], "foyer1");
		driver.assertTextEquals(Invite.CASES[0][2], "nom1");
		driver.assertTextEquals(Invite.CASES[0][3], "prenom1");
	}

	@Test
	public void test03Invite04modifInviteOk() throws InterruptedException {
		//

		//
		driver.executeScript("$('#invites').jqxGrid('begincelledit', 0, 'groupe');", 200);
		driver.type(By.id("textboxeditorinvitesgroupe"), "groupe1_modif", 100);
		driver.executeScript("$('#invites').jqxGrid('endcelledit', 0, 'groupe');", 500);
		driver.executeScript("$('#invites').jqxGrid('begincelledit', 0, 'nom');", 200);
		driver.type(By.id("textboxeditorinvitesnom"), "nom1_modif", 100);
		driver.executeScript("$('#invites').jqxGrid('endcelledit', 0, 'nom');", 500);
		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertTextEquals(Invite.CASES[0][0], "groupe1_modif");
		driver.assertTextEquals(Invite.CASES[0][2], "nom1_modif");
	}

	@Test
	public void test03Invite05modifPresenceOn() throws InterruptedException {
		//

		//
		driver.click(Invite.Input.LIGNE1_PRESENCES[0], 200);
		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], true);
	}

	@Test
	public void test03Invite06modifPresenceOff() throws InterruptedException {
		//

		//
		driver.click(Invite.Input.LIGNE1_PRESENCES[0], 200);
		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], false);
	}

	@Test
	public void test03Invite07modifPresenceAvecModele() throws InterruptedException {
		//

		//
		driver.click(Invite.Input.APPLIQUER_AU_FOYER, 200);
		driver.click(Invite.Input.MODELE_ETAPES[0], 200);
		driver.click(Invite.Input.MODELE_ETAPES[1], 200);

		driver.click(Invite.Button.LIGNE1_APPLIQUER_MODEL, 200);

		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], true);
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[1], true);
	}

	@Test
	public void test03Invite07modifPresenceAvecModele2() throws InterruptedException {
		//

		//
		driver.click(Invite.Input.APPLIQUER_AU_FOYER, 200);
		driver.click(Invite.Input.MODELE_ETAPES[0], 200);

		driver.click(Invite.Button.LIGNE1_APPLIQUER_MODEL, 200);

		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], true);
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[1], false);
	}

	@Test
	public void test03Invite08AjoutEnMasse() throws InterruptedException {
		//
		final String nom = "Nom1";
		final String prenom = "Prenom1";
		final String groupe = "Groupe1";
		final String adresse = "Adresse1";
		final String invites = nom + ":" + prenom + ":" + groupe + ":" + adresse
				+ "\nNom3:Prenom1:Groupe3;Adresse3\n\nNom2:Prenom2:Groupe2;Adresse2\nNom4:Prenom4:Groupe4;Adresse4";

		//
		driver.click(Invite.Button.AFFICHE_POPUP_EN_MASSE, 200);
		driver.type(Input.INVITE_EN_MASSE, invites, 200);
		driver.click(Invite.Button.AJOUT_EN_MASSE, 200);

		//
		driver.assertTextEquals(Invite.CASES[0][0], groupe);
		driver.assertTextEquals(Invite.CASES[0][1], "");
		driver.assertTextEquals(Invite.CASES[0][2], nom);
		driver.assertTextEquals(Invite.CASES[0][3], prenom);
	}

	@Test
	public void test04Admin01accesPage() throws InterruptedException {
		//

		//
		driver.click(Menu.LIEN_ADMINISTRATION, 1000);

		//
		driver.assertPageTitle("Mariage");
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		driver.assertTextEquals(Commun.TABLEAU_MESSAGE_VIDE, "No data to display");
		driver.assertElementPresent(Admin.Button.AFFICHE_POPUP);
	}

	@Test
	public void test04Admin02ajoutKo() throws InterruptedException {

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
	public void test04Admin03ajoutOk() throws InterruptedException {
		//

		//
		driver.type(Admin.Input.LOGIN, "monLogin", 200);
		driver.type(Admin.Input.MDP, "monMdp", 200);
		driver.click(Admin.Button.AJOUT, 500);

		//
		driver.assertTextEquals(Admin.CASES[0][0], "monLogin");
	}

}
