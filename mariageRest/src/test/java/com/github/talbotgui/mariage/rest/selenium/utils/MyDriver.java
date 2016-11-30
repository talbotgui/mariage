package com.github.talbotgui.mariage.rest.selenium.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Driver personnalisé pour simplifier l'écriture des tests Selenium
 */
public class MyDriver {

	private static final long DEFAULT_TIMEOUT = 10;

	private static final Logger LOG = LoggerFactory.getLogger(MyDriver.class);

	private static final int NB_MS_ATTENTE_SI_ASSERTION_ERROR = 5000;

	protected String contextPath;

	/** Le driver réel. */
	private WebDriver driver;

	protected int port;

	/**
	 * Constructeur
	 *
	 * @param port
	 * @param contextPath
	 */
	public MyDriver(final int port, final String contextPath) {
		this.port = port;
		this.contextPath = contextPath;
		this.initialize();
	}

	public void assertChecked(final By by, final boolean checked) {
		assertEquals(checked, this.driver.findElement(by).isSelected());
	}

	public void assertCookieNotPresentOrValid(final String cookieName) {
		final Cookie cookie = this.driver.manage().getCookieNamed(cookieName);
		Assert.assertTrue(cookie == null || cookie.getExpiry().before(new Date()));
	}

	public void assertCookiePresentAndValid(final String cookieName) {
		final Cookie cookie = this.driver.manage().getCookieNamed(cookieName);
		Assert.assertTrue(cookie != null
				&& (cookie.getExpiry() == null || cookie.getExpiry() != null && cookie.getExpiry().after(new Date())),
				"Le cookie " + cookieName + " n'est pas present ou pas valide");
	}

	public void assertElementNotPresent(final By by) {
		try {
			WebElement e = this.driver.findElement(by);
			this.sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			e = this.driver.findElement(by);
			Assert.fail("L'élement '" + by.toString() + "' est présent : " + e.toString());
		} catch (final NoSuchElementException e) {
			// OK
		}
	}

	public void assertElementNotVisible(final By by) {
		WebElement e = this.driver.findElement(by);
		this.sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
		e = this.driver.findElement(by);
		assertFalse("Element " + by.toString() + " is displayed", e.isDisplayed());
	}

	public void assertElementPresent(final By by) {
		assertNotNull(MyDriver.this.driver.findElement(by));
	}

	public void assertNumberOfElements(final By by, final int count) {
		try {
			assertEquals(this.driver.findElements(by).size(), count);
		} catch (final AssertionError e) {
			this.sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(this.driver.findElements(by).size(), count);
		}
	}

	public void assertNumberOfVisibleElementsWithClass(final String className, final int nbElementsAttendus) {
		List<WebElement> wes;
		try {
			wes = this.driver.findElements(By.xpath("." + className + ":visible"));
		} catch (final NoSuchElementException e) {
			wes = new ArrayList<>();
		}
		if (wes.size() != nbElementsAttendus) {
			fail("Nombre d'elements de la classe " + className + " attendus =" + nbElementsAttendus + ". Mais "
					+ wes.size() + " trouvé(s) : " + wes);
		}
	}

	public void assertPageTitle(final String title) {
		assertEquals(title, this.driver.getTitle());
	}

	public void assertPageTitleNot(final String title) {
		assertNotEquals(title, this.driver.getTitle());
	}

	public void assertTextEquals(final By by, final String text) {
		try {
			assertEquals(text, this.driver.findElement(by).getText());
		} catch (final AssertionError e) {
			this.sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(text, this.driver.findElement(by).getText());
		}
	}

	public void assertValueEquals(final By by, final String value) {
		try {
			assertEquals(value, this.driver.findElement(by).getAttribute("value"));
		} catch (final AssertionError e) {
			this.sleepSilencieux(NB_MS_ATTENTE_SI_ASSERTION_ERROR);
			assertEquals(value, this.driver.findElement(by).getAttribute("value"));
		}
	}

	public void click(final By by, final int timeToWait) {
		final WebElement we = this.driver.findElement(by);
		LOG.debug("Click sur le bouton {}", we);
		we.click();
		this.sleepSilencieux(timeToWait);
	}

	public void createSnapshot(final String filename) {
		try {
			final File scrFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
			final File file = new File(filename);
			FileUtils.copyFile(scrFile, file);
		} catch (final WebDriverException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	public void deleteAllCookies() {
		this.driver.manage().deleteAllCookies();
	}

	public void executeScript(final String script, final int timeToWait) {
		((JavascriptExecutor) this.driver).executeScript(script);
		this.sleepSilencieux(timeToWait);
	}

	/**
	 * Accès à une page
	 *
	 * @param url
	 *            URL complète commençant par http ou URI commençant pas /
	 */
	public void get(String url) {
		if (!url.startsWith("http")) {
			if (!url.startsWith("/")) {
				url = "/" + url;
			}
			url = "http://localhost:" + this.port + this.contextPath + url;
		}
		LOG.debug("driver get " + url);
		this.driver.get(url);
	}

	public WebDriver getRealDriver() {
		return this.driver;
	}

	private void initialize() {

		// Initialisation du vrai driver
		this.driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45, true);

		// Accès a la page sans cookie
		this.deleteAllCookies();
		this.get("http://localhost:" + this.port + this.contextPath + "/");

		// Maximisation de la fenetre
		this.driver.manage().window().maximize();

		// Initialisation du timeout par défaut
		this.driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

	}

	public void quit() {
		this.driver.quit();
	}

	public void select(final By by, final String visibleText, final int timeToWait) {
		new Select(this.driver.findElement(by)).selectByVisibleText(visibleText);
		this.sleepSilencieux(timeToWait);
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

		final WebElement e = this.driver.findElement(by);
		e.clear();
		e.sendKeys(value);
		this.sleepSilencieux(timeToWait);
	}

}
