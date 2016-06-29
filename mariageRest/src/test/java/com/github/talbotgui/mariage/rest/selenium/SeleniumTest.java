package com.github.talbotgui.mariage.rest.selenium;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
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
		// driver = new MyDriver(new HtmlUnitDriver(BrowserVersion.FIREFOX_38, true));
		driver = new MyDriver(new FirefoxDriver());
	}

	@Test
	public void test01Index01accesPage() throws InterruptedException {
		//
		driver.deleteAllCookies();

		//
		driver.get("http://localhost:" + port + contextPath + "/");

		//
		driver.assertPageTitle("Mariage");
		driver.assertElementNotPresent(By.linkText("Accueil"));
		driver.assertElementNotPresent(By.linkText("Invitation"));
		driver.assertElementPresent(By.linkText("Nouveau"));
		driver.assertElementPresent(By.id("selectionMariage"));
	}

	@Test
	public void test01Index01nouveauMariage() throws InterruptedException {
		//
		driver.click(By.linkText("Nouveau"), 500);
		driver.assertElementPresent(By.linkText("Sauvegarder"));

		//
		driver.type(By.id("dateCelebration"), "01/01/2017", 100);
		driver.click(By.id("marie1"), 0);// Pour fermer le timepicker
		driver.type(By.id("marie1"), "M", 100);
		driver.type(By.id("marie2"), "G", 100);
		driver.click(By.linkText("Sauvegarder"), 500);

		//
		driver.assertElementPresent(By.linkText("Modifier"));
		driver.assertTextEquals(By.id("maries"), "Mariage de M  &  G");
		driver.assertTextEquals(By.cssSelector("#date > span"), "01/01/2017");
		driver.assertElementPresent(By.linkText("Modifier"));
		driver.assertElementPresent(By.linkText("Accueil"));
	}

	@Test
	public void test01Index02modifierMariage() throws InterruptedException {
		//
		driver.click(By.linkText("Modifier"), 200);

		//
		driver.assertValueEquals(By.id("marie1"), "M");
		driver.assertValueEquals(By.id("marie2"), "G");
		driver.assertValueEquals(By.id("dateCelebration"), "01/01/2017");
		driver.click(By.linkText("Sauvegarder"), 500);

		//
		driver.assertElementPresent(By.linkText("Modifier"));
		driver.assertTextEquals(By.id("maries"), "Mariage de M  &  G");
		driver.assertTextEquals(By.cssSelector("#date > span"), "01/01/2017");
	}

	@Test
	public void test02ParametresEtape01Acces() throws InterruptedException {
		//

		//
		driver.click(By.linkText("Paramètres - étapes"), 200);

		//
		driver.assertTextEquals(By.id("maries"), "Mariage de M & G");
		driver.assertTextEquals(By.cssSelector("#date > span"), "01/01/2017");

		driver.assertTextEquals(By.xpath("//div[@id='columntableetapes']/div[1]/div/div"), "Type");
		driver.assertTextEquals(By.xpath("//div[@id='columntableetapes']/div[2]/div/div"), "Ordre");
		driver.assertTextEquals(By.xpath("//div[@id='columntableetapes']/div[3]/div/div"), "Nom");
		driver.assertTextEquals(By.xpath("//div[@id='columntableetapes']/div[4]/div/div"), "Date et heure");
		driver.assertTextEquals(By.xpath("//div[@id='columntableetapes']/div[5]/div/div"), "Lieu");
		driver.assertTextEquals(By.xpath("//div[@id='columntableetapes']/div[6]/div/div"), "Actions");

		driver.assertTextEquals(By.cssSelector("div.jqx-grid-cell.jqx-grid-empty-cell > span"), "No data to display");
	}

	@Test
	public void test02ParametresEtape02ValidationFormulaireRepas() throws InterruptedException {
		//

		//
		driver.click(By.id("button_afficher_popup_ajouter_repas"), 500);
		driver.type(By.id("repa_nom"), "", 15);
		driver.type(By.id("repa_lieu"), "", 15);
		driver.type(By.id("repa_dateHeure"), "", 15);
		driver.click(By.id("button_ajouter_etapeRepas"), 200);

		//
		assertEquals(driver.getRealDriver().findElements(By.className("error")).size(), 3);
	}

	@Test
	public void test02ParametresEtape03AjouterEtapeRepas() throws InterruptedException {
		//

		//
		driver.click(By.id("button_afficher_popup_ajouter_repas"), 500);
		driver.type(By.id("repa_nom"), "Repas1", 15);
		driver.type(By.id("repa_lieu"), "LieuRepas1", 15);
		driver.type(By.id("repa_dateHeure"), "01/01/2017 12:00", 15);
		driver.click(By.id("button_ajouter_etapeRepas"), 200);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row0etapes']/div[1]/div"), "EtapeRepas");
		driver.assertTextEquals(By.xpath("//div[@id='row0etapes']/div[2]/div"), "1");
		driver.assertTextEquals(By.xpath("//div[@id='row0etapes']/div[3]/div"), "Repas1");
		driver.assertTextEquals(By.xpath("//div[@id='row0etapes']/div[4]/div"), "01/01/2017 12:00");
		driver.assertTextEquals(By.xpath("//div[@id='row0etapes']/div[5]/div"), "LieuRepas1");
	}

	@Test
	public void test02ParametresEtape04ValidationFormulaireCeremonie() throws InterruptedException {
		//

		//
		driver.click(By.id("button_afficher_popup_ajouter_ceremonie"), 500);
		driver.type(By.id("cere_nom"), "", 15);
		driver.type(By.id("cere_lieu"), "", 15);
		driver.type(By.id("cere_dateHeure"), "", 15);
		driver.type(By.id("cere_celebrant"), "", 15);
		driver.click(By.id("button_ajouter_etapeCeremonie"), 200);

		//
		assertEquals(driver.getRealDriver().findElements(By.className("error")).size(), 4);
	}

	@Test
	public void test02ParametresEtape05AjouterEtapeCeremonie() throws InterruptedException {
		//

		//
		driver.click(By.id("button_afficher_popup_ajouter_ceremonie"), 500);
		driver.type(By.id("cere_nom"), "Ceremonie1", 15);
		driver.type(By.id("cere_lieu"), "LieuCeremonie1", 15);
		driver.type(By.id("cere_dateHeure"), "01/01/2017 13:00", 15);
		driver.type(By.id("cere_celebrant"), "Celebrant1", 15);
		driver.click(By.id("button_ajouter_etapeCeremonie"), 200);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row1etapes']/div[1]/div"), "EtapeCeremonie");
		driver.assertTextEquals(By.xpath("//div[@id='row1etapes']/div[2]/div"), "2");
		driver.assertTextEquals(By.xpath("//div[@id='row1etapes']/div[3]/div"), "Ceremonie1");
		driver.assertTextEquals(By.xpath("//div[@id='row1etapes']/div[4]/div"), "01/01/2017 13:00");
		driver.assertTextEquals(By.xpath("//div[@id='row1etapes']/div[5]/div"), "LieuCeremonie1");
	}

	@Test
	public void test03Invite01accesPage() throws InterruptedException {
		//

		//
		driver.click(By.linkText("Invitation"), 500);

		//
		driver.assertPageTitle("Mariage");
		driver.assertTextEquals(By.id("maries"), "Mariage de M & G");
		driver.assertTextEquals(By.cssSelector("#date > span"), "01/01/2017");
		driver.assertTextEquals(By.cssSelector("div.jqx-grid-cell.jqx-grid-empty-cell > span"), "No data to display");
		driver.assertElementPresent(By.id("button_afficher_popup_ajouter"));
	}

	@Test
	public void test03Invite02ajoutKo() throws InterruptedException {

		//
		driver.click(By.id("button_afficher_popup_ajouter"), 500);
		driver.assertElementPresent(By.id("button_ajouter"));
		driver.assertElementPresent(By.xpath("//div[@id='popupAjoutInvite']"));

		//
		driver.click(By.id("button_ajouter"), 500);

		//
		assertEquals(driver.getRealDriver().findElements(By.className("error")).size(), 4);
	}

	@Test
	public void test03Invite03ajoutOk() throws InterruptedException {
		//

		//
		driver.type(By.id("groupe"), "groupe1", 200);
		driver.type(By.id("foyer"), "foyer1", 200);
		driver.type(By.id("nom"), "nom1", 200);
		driver.type(By.id("prenom"), "prenom1", 200);
		driver.click(By.id("button_ajouter"), 500);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[1]/div"), "groupe1");
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[2]/div"), "foyer1");
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[3]/div"), "nom1");
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[4]/div"), "prenom1");
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
		driver.click(By.linkText("Invitation"), 500);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[1]/div"), "groupe1_modif");
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[3]/div"), "nom1_modif");
	}

	@Test
	public void test03Invite05modifPresenceOn() throws InterruptedException {
		//
		final String checkBox1 = "//div[@id='row0invites']/div[6]/div/input";

		//
		driver.click(By.xpath(checkBox1), 200);
		driver.click(By.linkText("Invitation"), 500);

		//
		driver.assertChecked(By.xpath(checkBox1), true);
	}

	@Test
	public void test03Invite06modifPresenceOff() throws InterruptedException {
		//
		final String checkBox1 = "//div[@id='row0invites']/div[6]/div/input";

		//
		driver.click(By.xpath(checkBox1), 200);
		driver.click(By.linkText("Invitation"), 500);

		//
		driver.assertChecked(By.xpath(checkBox1), false);
	}

	@Test
	public void test03Invite07modifPresenceAvecModele() throws InterruptedException {
		//

		//
		driver.click(By.id("appliquerAuFoyer"), 200);
		driver.click(By.xpath("//div[@id='modele']/div/table/tbody/tr/td[1]/input"), 200);
		driver.click(By.xpath("//div[@id='modele']/div/table/tbody/tr/td[2]/input"), 200);

		driver.click(By.xpath("//a[@id='btnApplyModel1']/span"), 200);

		driver.click(By.linkText("Invitation"), 500);

		//
		driver.assertChecked(By.xpath("//div[@id='row0invites']/div[6]/div/input"), true);
		driver.assertChecked(By.xpath("//div[@id='row0invites']/div[7]/div/input"), true);
	}

	@Test
	public void test03Invite07modifPresenceAvecModele2() throws InterruptedException {
		//

		//
		driver.click(By.id("appliquerAuFoyer"), 200);
		driver.click(By.xpath("//div[@id='modele']/div/table/tbody/tr/td[1]/input"), 200);

		driver.click(By.xpath("//a[@id='btnApplyModel1']/span"), 200);

		driver.click(By.linkText("Invitation"), 500);

		//
		driver.assertChecked(By.xpath("//div[@id='row0invites']/div[6]/div/input"), true);
		driver.assertChecked(By.xpath("//div[@id='row0invites']/div[7]/div/input"), false);
	}

	@Test
	public void test04Admin01accesPage() throws InterruptedException {
		//

		//
		driver.click(By.linkText("Administration"), 1000);

		//
		driver.assertPageTitle("Mariage");
		driver.assertTextEquals(By.id("maries"), "Mariage de M & G");
		driver.assertTextEquals(By.cssSelector("#date > span"), "01/01/2017");
		driver.assertTextEquals(By.cssSelector("div.jqx-grid-cell.jqx-grid-empty-cell > span"), "No data to display");
		driver.assertElementPresent(By.id("button_afficher_popup_ajouter_utilisateur"));
	}

	@Test
	public void test04Admin02ajoutKo() throws InterruptedException {

		//
		driver.click(By.id("button_afficher_popup_ajouter_utilisateur"), 500);
		driver.assertElementPresent(By.id("button_ajouter_utilisateur"));
		driver.assertElementPresent(By.xpath("//div[@id='popupAjoutUtilisateur']"));

		//
		driver.click(By.id("button_ajouter_utilisateur"), 500);

		//
		assertEquals(driver.getRealDriver().findElements(By.className("error")).size(), 2);
	}

	@Test
	public void test04Admin03ajoutOk() throws InterruptedException {
		//

		//
		driver.type(By.id("login"), "monLogin", 200);
		driver.type(By.id("mdp"), "monMdp", 200);
		driver.click(By.id("button_ajouter_utilisateur"), 500);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row0utilisateurs']/div[1]/div"), "monLogin");
	}

}
