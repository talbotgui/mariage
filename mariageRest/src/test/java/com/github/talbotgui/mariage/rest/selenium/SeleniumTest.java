package com.github.talbotgui.mariage.rest.selenium;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;

@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
public class SeleniumTest extends AbstractTestNGSpringContextTests {

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

		// Pour tester sur l'IC
		final boolean asIC = false;
		if (!SystemUtils.IS_OS_WINDOWS || asIC) {
			driver = new MyDriver(new HtmlUnitDriver(BrowserVersion.FIREFOX_38, true));
		}

		// Pour tester sur mon poste
		else {
			driver = new MyDriver(new FirefoxDriver());
		}
	}

	@Test
	public void test01Index01accesPage() throws InterruptedException {
		//
		driver.deleteAllCookies();

		//
		driver.get("http://localhost:" + port + "/");

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
		driver.assertElementPresent(By.linkText("Paramètres"));
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
	public void test02Invite01accesPage() throws InterruptedException {
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
	public void test02Invite02ajoutKo() throws InterruptedException {

		//
		driver.click(By.id("button_afficher_popup_ajouter"), 500);
		driver.assertElementPresent(By.id("button_ajouter"));
		driver.assertElementPresent(By.xpath("//div[@id='popupAjoutInvite']"));

		//
		driver.click(By.id("button_ajouter"), 500);

		//
		assertEquals(driver.getRealDriver().findElements(By.className("error")).size(), 3);
	}

	@Test
	public void test02Invite03ajoutOk() throws InterruptedException {
		//

		//
		driver.type(By.id("nom"), "nom1", 200);
		driver.type(By.id("prenom"), "prenom1", 200);
		driver.type(By.id("groupe"), "groupe1", 200);
		driver.click(By.id("button_ajouter"), 500);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[1]/div"), "nom1");
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[2]/div"), "groupe1");
	}

	@Test
	public void test02Invite04modifOk() throws InterruptedException {
		//

		//
		driver.executeScript("$('#invites').jqxGrid('begincelledit', 0,'nom');", 200);
		driver.type(By.id("textboxeditorinvitesnom"), "nom1_modif", 100);
		driver.executeScript("$('#invites').jqxGrid('endcelledit', 0,'nom');", 500);
		driver.executeScript("$('#invites').jqxGrid('begincelledit', 0,'groupe');", 200);
		driver.type(By.id("textboxeditorinvitesgroupe"), "groupe1_modif", 100);
		driver.executeScript("$('#invites').jqxGrid('endcelledit', 0,'groupe');", 500);
		driver.click(By.linkText("Invitation"), 500);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[1]/div"), "nom1_modif");
		driver.assertTextEquals(By.xpath("//div[@id='row0invites']/div[2]/div"), "groupe1_modif");
	}

	@Test
	public void test03Admin01accesPage() throws InterruptedException {
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
	public void test03Admin02ajoutKo() throws InterruptedException {

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
	public void test03Admin03ajoutOk() throws InterruptedException {
		//

		//
		driver.type(By.id("login"), "monLogin", 200);
		driver.type(By.id("mdp"), "monMdp", 200);
		driver.click(By.id("button_ajouter_utilisateur"), 500);

		//
		driver.assertTextEquals(By.xpath("//div[@id='row0utilisateurs']/div[1]/div"), "monLogin");
	}

}
