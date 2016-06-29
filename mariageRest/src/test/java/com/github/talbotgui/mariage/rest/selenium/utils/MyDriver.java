package com.github.talbotgui.mariage.rest.selenium.utils;

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
import org.openqa.selenium.support.ui.Select;
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

	public void assertChecked(final By by, final boolean checked) {
		try {
			assertEquals(checked, driver.findElement(by).isSelected());
		} catch (final AssertionError e) {
			sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
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

	public void assertElementPresent(final By by) {
		try {
			assertNotNull(driver.findElement(by));
		} catch (final AssertionError e) {
			sleepSilencieux(1000);
			assertNotNull(driver.findElement(by));
		}
	}

	public void assertPageTitle(final String title) {
		try {
			assertEquals(title, driver.getTitle());
		} catch (final AssertionError e) {
			sleepSilencieux(1000);
			assertEquals(title, driver.getTitle());
		}
	}

	public void assertTextEquals(final By by, final String text) {
		try {
			assertEquals(text, driver.findElement(by).getText());
		} catch (final AssertionError e) {
			sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(text, driver.findElement(by).getText());
		}
	}

	public void assertValueEquals(final By by, final String value) {
		try {
			assertEquals(value, driver.findElement(by).getAttribute("value"));
		} catch (final AssertionError e) {
			sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(value, driver.findElement(by).getAttribute("value"));
		}
	}

	public void click(final By by, final int timeToWait) {
		driver.findElement(by).click();
		sleepSilencieux(timeToWait);
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

	public void executeScript(final String script, final int timeToWait) {
		((JavascriptExecutor) driver).executeScript(script);
		sleepSilencieux(timeToWait);
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

	public void select(final By by, final String visibleText, final int timeToWait) {
		new Select(this.driver.findElement(by)).selectByVisibleText(visibleText);
		sleepSilencieux(timeToWait);
	}

	private void sleepSilencieux(final int timeToWait) {
		try {
			Thread.sleep(timeToWait);
		} catch (final InterruptedException e) {
			// Rien à faire
		}
	}

	public void type(final By by, final String value, final int timeToWait) {
		this.assertElementPresent(by);

		final WebElement e = driver.findElement(by);
		e.clear();
		e.sendKeys(value);
		sleepSilencieux(timeToWait);
	}
}
