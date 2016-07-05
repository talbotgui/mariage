package com.github.talbotgui.mariage.rest.selenium.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import javax.sql.DataSource;

import org.junit.Assert;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class SeleniumTest extends AbstractTestNGSpringContextTests {

	private static final Logger LOG = LoggerFactory.getLogger(SeleniumTest.class);

	/** ContextRoot de l'application. */
	@Value("${server.context-path}")
	protected String contextPath;

	@Autowired
	private DataSource dataSource;

	protected MyDriver driver;

	/** Port de l'application web aléatoire injecté par Spring. */
	@Value("${local.server.port}")
	protected int port;

	@AfterClass
	public void afterClass() throws Exception {
		driver.quit();
	}

	@BeforeClass
	public void beforeClass() {

		//
		final String[] jdds = getJeuDeDonnees();
		for (final String jdd : jdds) {
			try {
				final URI uri = ClassLoader.getSystemResource(jdd).toURI();
				final Collection<String> strings = Files.readAllLines(Paths.get(uri));
				final String[] requetes = strings.toArray(new String[strings.size()]);
				final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
				LOG.info("Execute SQL : {}", (Object[]) requetes);
				jdbc.batchUpdate(requetes);
			} catch (DataAccessException | URISyntaxException | IOException e) {
				LOG.error("Echec durant l'injection du jeu de données '{}'", jdd);
				Assert.fail(e.getMessage());
			}
		}

		//
		driver = new MyDriver(/* new HtmlUnitDriver(BrowserVersion.FIREFOX_45, true) */new FirefoxDriver());
		driver.deleteAllCookies();
		driver.get("http://localhost:" + port + contextPath + "/");

	}

	public abstract String[] getJeuDeDonnees();

}
