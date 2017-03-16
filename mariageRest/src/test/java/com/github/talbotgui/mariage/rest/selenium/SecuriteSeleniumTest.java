package com.github.talbotgui.mariage.rest.selenium;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Login;
import com.github.talbotgui.mariage.rest.selenium.utils.Selectors.Menu;

@SpringBootTest(classes = SeleniumTestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SecuriteSeleniumTest extends SeleniumTest {

	@DataProvider(name = "listeDesPagesVideSiPasAdmin")
	public static Object[][] getListeDesPagesVideSiPasAdmin() {
		return new Object[][] { { "administration.html" }, { "parametresEtape.html" }, { "parametresCourrier.html" } };
	}

	@Override
	public String[] getJeuDeDonnees() {
		return new String[] { "sql/dataPurge.sql", "sql/dataSet_justeUnMariage.sql" };
	}

	@Test
	@Override
	public void test00login() {
		// Creation d'un utilisateur simple (ni marié ni admin)
		final String loginSimpleUtilisateur = "loginSimpleUtilisateur";
		final String mdpSimpleUtilisateur = "mdpSimpleUtilisateur";
		super.securiteService.sauvegarderUtilisateur(loginSimpleUtilisateur, mdpSimpleUtilisateur,
				Utilisateur.Role.UTILISATEUR);

		// Connexion avec cet utilisateur
		this.driver.assertPageTitle(Login.TITRE_PAGE);
		this.driver.type(Login.Input.LOGIN, loginSimpleUtilisateur, 200);
		this.driver.type(Login.Input.MDP, mdpSimpleUtilisateur, 200);
		this.driver.click(Login.Button.LOGIN, 800);
		this.driver.assertCookiePresentAndValid(Login.Cookie.JSESSIONID);
		this.driver.assertPageTitle("Mariage");

	}

	@Test(dataProvider = "listeDesPagesVideSiPasAdmin")
	public void test01AccesPageAuContenuNonAutorise(final String page) {

		// Accès à la page
		this.driver.get(page);

		// Validation que aucun contenu n'est visible dans la DIV .content
		this.driver.assertNumberOfVisibleElementsIn(".content", 0);
	}

	@Test
	@Override
	public void test99logout() {
		this.driver.click(Menu.LIEN_DECONNEXION, 800);
	}

}
