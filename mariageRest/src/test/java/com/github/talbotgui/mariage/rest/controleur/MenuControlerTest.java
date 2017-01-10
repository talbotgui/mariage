package com.github.talbotgui.mariage.rest.controleur;

import java.util.ArrayList;
import java.util.Collection;

import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.googlecode.catchexception.CatchException;

public class MenuControlerTest extends BaseRestControlerTest {

	@DataProvider(name = "getTestParameters")
	public Object[][] getTestParameters() {

		final Collection<String> liensCommun = new ArrayList<>();
		liensCommun.add("index.html");
		liensCommun.add("aide.html");
		liensCommun.add("parametresEtape.htm");
		liensCommun.add("parametresCourrier.html");
		liensCommun.add("invites.html");
		liensCommun.add("contact.html");
		liensCommun.add("invitations.html");
		liensCommun.add("envois.html");
		liensCommun.add("reponses.html");
		liensCommun.add("logout");

		final Collection<String> liensMaries = new ArrayList<>(liensCommun);
		liensMaries.add("statistiques.html");

		final Collection<String> liensAdmin = new ArrayList<>(liensMaries);
		liensAdmin.add("administration.html");

		return new Object[][] { { Role.ADMIN, Boolean.TRUE, liensAdmin }, { Role.MARIE, Boolean.TRUE, liensMaries },
				{ Role.UTILISATEUR, Boolean.TRUE, liensCommun } };
	}

	@Test
	public void test01AccesMenuSansUtilisateurConnecte() {

		// ARRANGE

		// ACT
		CatchException.catchException(this.getREST()).exchange(this.getURL() + "/part_menu.html", HttpMethod.GET, null,
				String.class);

		// ASSERT
		final Exception e = CatchException.caughtException();
		Assert.assertNotNull(e);
		Assert.assertTrue(e instanceof HttpStatusCodeException);
		Assert.assertEquals(((HttpStatusCodeException) e).getStatusCode(), HttpStatus.NOT_FOUND);

	}

	@Test(dataProvider = "getTestParameters")
	public void test02AccesMenuAvecAdminConnecte(final Role role, final Boolean avecMariageSelectionne,
			final Collection<String> liens) {

		// ARRANGE
		final String login = "monLogin";
		final String mdp = "monMdp";
		Mockito.doReturn(Role.ADMIN).when(this.securiteService).verifierUtilisateur(Mockito.anyString(),
				Mockito.anyString());
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("login", login, "mdp",
				mdp);
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", "idMariage=4");
		final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(requestParam, headers);
		final ResponseEntity<Void> response = this.getREST().exchange(this.getURL() + "/dologin", HttpMethod.POST,
				request, Void.class);
		String cookie = response.getHeaders().get("Set-Cookie").get(0);
		cookie = cookie.substring(0, cookie.indexOf(";"));

		if (avecMariageSelectionne) {
			cookie += ";idMariage=4";
		}

		// ACT
		final HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", cookie);
		final HttpEntity<String> requestMenu = new HttpEntity<String>(requestHeaders);
		final ResponseEntity<String> reponseMenu = this.getREST().exchange(this.getURL() + "/part_menu.html",
				HttpMethod.GET, requestMenu, String.class);

		// ASSERT
		Assert.assertEquals(reponseMenu.getStatusCode(), HttpStatus.OK);
		final String body = reponseMenu.getBody();
		for (final String lien : liens) {
			Assert.assertTrue(body.contains(lien), "Présence du lien '" + lien + "'");
		}
	}

}
