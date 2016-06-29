package com.github.talbotgui.mariage.rest.selenium;

import org.openqa.selenium.By;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Commun;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Index;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Invite;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Invite.Input;
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
		driver.select(Index.Input.SELECTION_MARIAGE, "M & G", 100);
		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertPageTitle("Mariage");
		driver.assertTextEquals(Commun.DIV_MARIES, "Mariage de M & G");
		driver.assertTextEquals(Commun.DIV_DATE_MARIAGE, "01/01/2017");
		driver.assertTextEquals(Commun.TABLEAU_MESSAGE_VIDE, "No data to display");
		driver.assertElementPresent(Invite.Button.AFFICHE_POPUP_SAISIE);
	}

	@Test
	public void test02ajoutKo() {

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
	public void test03ajoutOk() {
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
	public void test04modifInviteOk() {
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
	public void test05modifPresenceOn() {
		//

		//
		driver.click(Invite.Input.LIGNE1_PRESENCES[0], 200);
		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], true);
	}

	@Test
	public void test06modifPresenceOff() {
		//

		//
		driver.click(Invite.Input.LIGNE1_PRESENCES[0], 200);
		driver.click(Menu.LIEN_INVITATION, 500);

		//
		driver.assertChecked(Invite.Input.LIGNE1_PRESENCES[0], false);
	}

	@Test
	public void test07modifPresenceAvecModele() {
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
	public void test07modifPresenceAvecModele2() {
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
	public void test08AjoutEnMasse() {
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

}
