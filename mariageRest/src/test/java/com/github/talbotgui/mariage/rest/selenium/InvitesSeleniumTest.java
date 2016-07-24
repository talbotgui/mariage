package com.github.talbotgui.mariage.rest.selenium;

import org.openqa.selenium.By;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Invite;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;
import com.github.talbotgui.mariage.rest.selenium.utils.SeleniumTest;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SeleniumTestApplication.class)
public class InvitesSeleniumTest extends SeleniumTest {

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] { "sql/dataPurge.sql", "sql/dataSet_mariageEtEtapes.sql" };
	}

	@Test
	public void test01accesPage() {
		//

		//
		this.driver.select(Index.Input.SELECTION_MARIAGE, "M & G", 100);
		this.driver.click(Menu.LIEN_INVITATION, 500);

		//
		this.driver.assertPageTitle("Mariage");
		this.driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		this.driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		this.driver.assertTextEquals(Commun.TABLEAU_MESSAGE_VIDE, "No data to display");
		this.driver.assertElementPresent(Invite.Button.AFFICHE_POPUP_SAISIE);
	}

	@Test
	public void test02ajoutKo() {

		//
		this.driver.click(Invite.Button.AFFICHE_POPUP_SAISIE, 500);
		this.driver.assertElementPresent(Invite.Button.AJOUTER);
		this.driver.assertElementPresent(Invite.DIV_POPUP);

		//
		this.driver.click(Invite.Button.AJOUTER, 500);

		//
		this.driver.count(Commun.DIV_ERREUR, 4);
	}

	@Test
	public void test03ajoutOk() {
		//

		//
		this.driver.type(Invite.Input.GROUPE, "groupe1", 200);
		this.driver.type(Invite.Input.FOYER, "foyer1", 200);
		this.driver.type(Invite.Input.NOM, "nom1", 200);
		this.driver.type(Invite.Input.PRENOM, "prenom1", 200);
		this.driver.click(Invite.Button.AJOUTER, 500);

		//
		this.driver.assertTextEquals(Invite.CASES[0][0], "groupe1");
		this.driver.assertTextEquals(Invite.CASES[0][1], "foyer1");
		this.driver.assertTextEquals(Invite.CASES[0][2], "nom1");
		this.driver.assertTextEquals(Invite.CASES[0][3], "prenom1");
	}

	@Test
	public void test04modifInviteOk() {
		//

		//
		this.driver.executeScript("$('#invites').jqxGrid('begincelledit', 0, 'groupe');", 200);
		this.driver.type(By.id("textboxeditorinvitesgroupe"), "groupe1_modif", 100);
		this.driver.executeScript("$('#invites').jqxGrid('endcelledit', 0, 'groupe');", 500);
		this.driver.executeScript("$('#invites').jqxGrid('begincelledit', 0, 'nom');", 200);
		this.driver.type(By.id("textboxeditorinvitesnom"), "nom1_modif", 100);
		this.driver.executeScript("$('#invites').jqxGrid('endcelledit', 0, 'nom');", 500);
		this.driver.click(Menu.LIEN_INVITATION, 500);

		//
		this.driver.assertTextEquals(Invite.CASES[0][0], "groupe1_modif");
		this.driver.assertTextEquals(Invite.CASES[0][2], "nom1_modif");
		this.driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], false);
	}

	@Test
	public void test05modifPresenceOn() {
		//
		this.driver.click(Menu.LIEN_INVITATION, 500);

		//
		this.driver.click(Invite.Input.LIGNE1_PRESENCES[0], 200);

		//
		this.driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], true);
	}

	@Test
	public void test06modifPresenceOff() {
		//
		this.driver.click(Menu.LIEN_INVITATION, 500);

		//
		this.driver.click(Invite.Input.LIGNE1_PRESENCES[0], 200);

		//
		this.driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], false);
	}

	@Test
	public void test07modifPresenceAvecModele() {
		//

		//
		this.driver.click(Invite.Input.APPLIQUER_AU_FOYER, 200);
		this.driver.click(Invite.Input.MODELE_ETAPES[0], 200);
		this.driver.click(Invite.Input.MODELE_ETAPES[1], 200);

		this.driver.click(Invite.Button.LIGNE1_APPLIQUER_MODEL, 200);

		this.driver.click(Menu.LIEN_INVITATION, 500);

		//
		this.driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], true);
		this.driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[1], true);
	}

	@Test
	public void test07modifPresenceAvecModele2() {
		//

		//
		this.driver.click(Invite.Input.APPLIQUER_AU_FOYER, 200);
		this.driver.click(Invite.Input.MODELE_ETAPES[0], 200);

		this.driver.click(Invite.Button.LIGNE1_APPLIQUER_MODEL, 200);

		this.driver.click(Menu.LIEN_INVITATION, 500);

		//
		this.driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], true);
		this.driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[1], false);
	}

}
