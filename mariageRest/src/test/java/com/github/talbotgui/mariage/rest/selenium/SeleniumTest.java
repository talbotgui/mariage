package com.github.talbotgui.mariage.rest.selenium;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import javax.sql.DataSource;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.service.SecuriteService;
import com.github.talbotgui.mariage.rest.application.RestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.MyDriver;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Login;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;

public abstract class SeleniumTest extends AbstractTestNGSpringContextTests {
	private static final Logger LOG = LoggerFactory.getLogger(SeleniumTest.class);

	/** ContextRoot de l'application. */
	@Value("${server.context-path}")
	private String contextPath;

	@Autowired
	private DataSource dataSource;

	protected MyDriver driver;

	/** Port de l'application web aléatoire injecté par Spring. */
	@Value("${local.server.port}")
	private int port;

	@Autowired
	protected SecuriteService securiteService;

	@AfterClass
	public void afterClass() throws Exception {
		this.driver.quit();
	}

	@BeforeClass
	public void beforeClass() {

		//
		final String[] jdds = this.getJeuDeDonnees();
		if (jdds != null) {
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
		}

		// creation du driver
		this.driver = new MyDriver(this.port, this.contextPath);

		// creation d'un user si aucun en base dans le jeu de donnees
		if (this.securiteService.listerUtilisateurs().isEmpty()) {
			this.securiteService.sauvegarderUtilisateur(RestApplication.LOGIN_MDP_ADMIN_PAR_DEFAUT,
					RestApplication.LOGIN_MDP_ADMIN_PAR_DEFAUT, Utilisateur.Role.ADMIN);
		}
	}

	public abstract String[] getJeuDeDonnees();

	public void test00login() {

		// Arrange
		this.driver.assertPageTitle(Login.TITRE_PAGE);

		// Act
		this.driver.type(Login.Input.LOGIN, RestApplication.LOGIN_MDP_ADMIN_PAR_DEFAUT, 200);
		this.driver.type(Login.Input.MDP, RestApplication.LOGIN_MDP_ADMIN_PAR_DEFAUT, 200);
		this.driver.click(Login.Button.LOGIN, 800);

		// Assert
		this.driver.assertCookiePresentAndValid(Login.Cookie.JSESSIONID);
		this.driver.assertPageTitle("Mariage");
	}

	public void test99logout() {

		// Arrange

		// Act
		this.driver.click(Menu.LIEN_DECONNEXION, 800);

		// Assert
		this.driver.assertPageTitle(Login.TITRE_PAGE);
	}

}
