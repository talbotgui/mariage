package com.github.talbotgui.mariage.rest.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 * Driver personnalisé pour simplifier l'écriture des tests Selenium
 */
public class MyDriver {

	private static final long DEFAULT_TIMEOUT = 10;

	private static final int NB_MS_ATTENTE_SI_ASSERTION_ERROR = 5000;

	/** Le driver réel. */
	private final WebDriver driver;

	/**
	 * Constructeur avec initialisation
	 *
	 * @param driver
	 *            Le vrai driver
	 */
	public MyDriver(final WebDriver driver) {
		super();
		this.driver = driver;

		// Initialisation du timeout par défaut
		driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
	}

	public void assertChecked(final By by, final boolean checked) throws InterruptedException {
		try {
			assertEquals(checked, driver.findElement(by).isSelected());
		} catch (final AssertionError e) {
			Thread.sleep(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(checked, driver.findElement(by).isSelected());
		}
	}

	public void assertElementNotPresent(final By by) {
		try {
			driver.findElement(by);
			Assert.fail("L'élement '" + by.toString() + "' est présent");
		} catch (final NoSuchElementException e) {
			// OK
		}
	}

	public void assertElementPresent(final By by) throws InterruptedException {
		try {
			assertNotNull(driver.findElement(by));
		} catch (final AssertionError e) {
			Thread.sleep(1000);
			assertNotNull(driver.findElement(by));
		}
	}

	public void assertPageTitle(final String title) throws InterruptedException {
		try {
			assertEquals(title, driver.getTitle());
		} catch (final AssertionError e) {
			Thread.sleep(1000);
			assertEquals(title, driver.getTitle());
		}
	}

	public void assertTextEquals(final By by, final String text) throws InterruptedException {
		try {
			assertEquals(text, driver.findElement(by).getText());
		} catch (final AssertionError e) {
			Thread.sleep(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(text, driver.findElement(by).getText());
		}
	}

	public void assertValueEquals(final By by, final String value) throws InterruptedException {
		try {
			assertEquals(value, driver.findElement(by).getAttribute("value"));
		} catch (final AssertionError e) {
			Thread.sleep(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(value, driver.findElement(by).getAttribute("value"));
		}
	}

	public void click(final By by, final long timeToWait) throws InterruptedException {
		driver.findElement(by).click();
		Thread.sleep(timeToWait);
	}

	public void count(final By by, final int count) {
		assertEquals(this.driver.findElements(by).size(), count);
	}

	public void createSnapshot(final String filename) throws IOException {
		final File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		final File file = new File(filename);
		FileUtils.copyFile(scrFile, file);
	}

	public void deleteAllCookies() {
		this.driver.manage().deleteAllCookies();
	}

	public void executeScript(final String script, final long timeToWait) throws InterruptedException {
		((JavascriptExecutor) driver).executeScript(script);
		Thread.sleep(timeToWait);
	}

	public void get(final String baseUrl) {
		this.driver.get(baseUrl);
	}

	public WebDriver getRealDriver() {
		return driver;
	}

	public void quit() {
		this.driver.quit();
	}

	public void type(final By by, final String value, final int timeToWait) throws InterruptedException {
		this.assertElementPresent(by);

		final WebElement e = driver.findElement(by);
		e.clear();
		e.sendKeys(value);
		Thread.sleep(timeToWait);
	}
}
