package com.github.talbotgui.mariage.rest.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumTest {

	private static final String BASE_URL = "http://localhost";

	private static final Logger LOG = LoggerFactory.getLogger(SeleniumTest.class);

	private WebDriver driver;

	@AfterClass
	public void afterClass() throws Exception {
		driver.quit();
	}

	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void takeScreenShotOnFailure(final ITestResult testResult) throws IOException {
		if (ITestResult.FAILURE == testResult.getStatus()) {
			final File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			final File file = new File("SeleniumErrorScreenShot_" + testResult.getName() + ".jpg");
			FileUtils.copyFile(scrFile, file);
			LOG.error("Error during test {} saved in screenshot {}", testResult.getName(), file);
		}
	}

	@Test
	public void test01Index() throws InterruptedException {

		driver.manage().deleteAllCookies();
		driver.get(BASE_URL + "/");
		assertEquals(driver.getTitle(), "Mariage");
		assertNotNull(driver.findElement(By.linkText("Nouveau")));
		assertNotNull(driver.findElement(By.id("selectionMariage")));

		// Test nouveau
		driver.findElement(By.linkText("Nouveau")).click();
		Thread.sleep(5000);
		assertNotNull(driver.findElement(By.linkText("Sauvegarder")));

		driver.findElement(By.id("dateCelebration")).clear();
		driver.findElement(By.id("dateCelebration")).sendKeys("01/01/2017");
		Thread.sleep(200);
		driver.findElement(By.id("marie1")).click();
		driver.findElement(By.id("marie1")).clear();
		driver.findElement(By.id("marie1")).sendKeys("M");
		Thread.sleep(200);
		driver.findElement(By.id("marie2")).click();
		driver.findElement(By.id("marie2")).clear();
		driver.findElement(By.id("marie2")).sendKeys("G");
		Thread.sleep(500);

		driver.findElement(By.linkText("Sauvegarder")).click();
		Thread.sleep(500);
		assertNotNull(driver.findElement(By.linkText("Modifier")));

		assertEquals(driver.findElement(By.id("maries")).getText(), "Mariage de M  &  G");
		assertEquals(driver.findElement(By.cssSelector("#date > span")).getText(), "01/01/2017");
		assertNotNull(driver.findElement(By.linkText("Modifier")));
		assertNotNull(driver.findElement(By.linkText("Accueil")));
		assertNotNull(driver.findElement(By.linkText("Paramètres")));

		// Test modifier
		driver.findElement(By.linkText("Modifier")).click();
		assertEquals(driver.findElement(By.id("marie1")).getAttribute("value"), "M");
		assertEquals(driver.findElement(By.id("marie2")).getAttribute("value"), "G");
		assertEquals(driver.findElement(By.id("dateCelebration")).getAttribute("value"), "01/01/2017");
		driver.findElement(By.linkText("Sauvegarder")).click();
		Thread.sleep(500);
		assertNotNull(driver.findElement(By.linkText("Modifier")));
	}

	@Test
	public void test02Invite() throws InterruptedException {

		driver.findElement(By.linkText("Invitation")).click();
		Thread.sleep(500);
		assertNotNull(driver.findElement(By.id("button_afficher_popup_ajouter")));

		assertEquals(driver.getTitle(), "Mariage");
		assertEquals(driver.findElement(By.id("maries")).getText(), "Mariage de M & G");
		assertEquals(driver.findElement(By.cssSelector("#date > span")).getText(), "01/01/2017");
		assertEquals(driver.findElement(By.cssSelector("div.jqx-grid-cell.jqx-grid-empty-cell > span")).getText(),
				"No data to display");
		assertNotNull(driver.findElement(By.id("button_afficher_popup_ajouter")));

		// Test ajout KO
		driver.findElement(By.id("button_afficher_popup_ajouter")).click();

		Thread.sleep(500);
		assertNotNull(driver.findElement(By.id("button_ajouter")));
		assertNotNull(driver.findElement(By.xpath("//div[@id='popupAjoutInvite']")));

		driver.findElement(By.id("button_ajouter")).click();
		Thread.sleep(500);
		assertEquals(driver.findElements(By.className("error")).size(), 3);

		// Test ajout OK
		driver.findElement(By.id("nom")).clear();
		driver.findElement(By.id("nom")).sendKeys("nom1");
		Thread.sleep(200);
		driver.findElement(By.id("prenom")).clear();
		driver.findElement(By.id("prenom")).sendKeys("prenom1");
		driver.findElement(By.id("groupe")).clear();
		Thread.sleep(200);
		driver.findElement(By.id("groupe")).sendKeys("groupe1");
		Thread.sleep(200);

		driver.findElement(By.id("button_ajouter")).click();
		Thread.sleep(500);
		assertEquals("nom1", driver.findElement(By.xpath("//div[@id='row0invites']/div[1]/div")).getText());
		assertEquals("groupe1", driver.findElement(By.xpath("//div[@id='row0invites']/div[2]/div")).getText());

		// Test modification
		((JavascriptExecutor) driver).executeScript("$('#invites').jqxGrid('begincelledit', 0,'nom');");
		driver.findElement(By.id("textboxeditorinvitesnom")).clear();
		driver.findElement(By.id("textboxeditorinvitesnom")).sendKeys("nom1_modif");
		((JavascriptExecutor) driver).executeScript("$('#invites').jqxGrid('endcelledit', 0,'nom');");
		Thread.sleep(200);
		((JavascriptExecutor) driver).executeScript("$('#invites').jqxGrid('begincelledit', 0,'groupe');");
		driver.findElement(By.id("textboxeditorinvitesgroupe")).clear();
		driver.findElement(By.id("textboxeditorinvitesgroupe")).sendKeys("groupe1_modif");
		((JavascriptExecutor) driver).executeScript("$('#invites').jqxGrid('endcelledit', 0,'groupe');");
		Thread.sleep(200);

		driver.findElement(By.linkText("Invitation")).click();
		Thread.sleep(500);
		assertEquals("nom1_modif", driver.findElement(By.xpath("//div[@id='row0invites']/div[1]/div")).getText());
		assertEquals("groupe1_modif", driver.findElement(By.xpath("//div[@id='row0invites']/div[2]/div")).getText());

	}
}
