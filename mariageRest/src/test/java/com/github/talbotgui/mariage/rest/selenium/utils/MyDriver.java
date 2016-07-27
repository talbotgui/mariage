package com.github.talbotgui.mariage.rest.selenium.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Driver personnalisé pour simplifier l'écriture des tests Selenium
 */
public class MyDriver {

	private static final long DEFAULT_TIMEOUT = 10;
	
	private static final int NB_MS_ATTENTE_SI_ASSERTION_ERROR = 5000;

	/** Chemin vers le binaire de PhantomJS sur un poste Windows. */
	private static final String PHANTOMJS_BINARY_PATH_FOR_WINDOWS = "D:\\Outils\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";

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
		Assert.assertTrue(cookie != null && cookie.getExpiry().after(new Date()));
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

	public void assertElementPresent(final By by) {
		assertNotNull(MyDriver.this.driver.findElement(by));
	}

	public void assertPageTitle(final String title) {
		assertEquals(title, this.driver.getTitle());
	}

	public void assertTextEquals(final By by, final String text) {
		assertEquals(text, this.driver.findElement(by).getText());
	}

	public void assertValueEquals(final By by, final String value) {
		assertEquals(value, this.driver.findElement(by).getAttribute("value"));
	}

	public void click(final By by, final int timeToWait) {
		this.driver.findElement(by).click();
		this.sleepSilencieux(timeToWait);
	}

	public void count(final By by, final int count) {
		assertEquals(this.driver.findElements(by).size(), count);
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
	public void get(final String url) {
		if (url.startsWith("http")) {
			this.driver.get(url);
		} else {
			this.driver.get("http://localhost:" + this.port + this.contextPath + url);
		}
	}

	public WebDriver getRealDriver() {
		return this.driver;
	}

	private void initialize() {

		// Initialisation du vrai driver
		if ((new File(PHANTOMJS_BINARY_PATH_FOR_WINDOWS)).exists()) {
			final DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true);
			caps.setCapability("takesScreenshot", true);
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					PHANTOMJS_BINARY_PATH_FOR_WINDOWS);
			this.driver = new PhantomJSDriver(caps);
		} else {
			this.driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45, true);
		}

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
