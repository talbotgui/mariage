package com.github.talbotgui.mariage.rest.security;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.rest.application.SeleniumTestApplication;
import com.github.talbotgui.mariage.rest.controleur.BaseWebTest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.googlecode.catchexception.CatchException;

@SpringBootTest(classes = SeleniumTestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class DroitAccesAPITest extends BaseWebTest {

	@DataProvider(name = "getTestParameters")
	public Object[][] getTestParameters() throws IOException {

		// Appel à Swagger pour récupérer la liste des URL exposés par
		// l'application
		final String url = super.getURL() + "/v2/api-docs";
		final ResponseEntity<Object> infoApi = super.getREST().getForEntity(url, Object.class);

		String reponse = infoApi.getBody().toString();
		// " autour des valeurs
		reponse = reponse.replaceAll("=([^,{=}]*)([,}])", "=\"$1\"$2");
		// " autour des clef
		reponse = reponse.replaceAll("([{, ]*)([^,={}]*)=", "$1\"$2\"=");
		// " autour des clef de type URL comme /qsd/{param}
		reponse = reponse.replaceAll(", (/[a-zA-Z0-9/{},?]*)\"\"=", ",\"$1\"=");
		// pour traiter les inter-actions des deux précédentes regex
		reponse = reponse.replaceAll(", (/[a-zA-Z0-9/{},?]*)\"/([a-zA-Z]*)\"=", ",\"$1/$2\"=");
		reponse = reponse.replaceAll("\"produces\"=\"\\[text/html;charset\"=\"UTF-8\\]\"",
				"\"produces\"=\"[text/html;charset=UTF-8]\"");

		// Transformation JSON, récupération des URLs et préparation des params
		// pour la méthode de test
		final Collection<Object[]> parametresMethodeDeTest = new ArrayList<>();
		try (JsonReader jr = new JsonReader(new StringReader(reponse))) {
			jr.setLenient(true);
			final JsonObject obj = (new JsonParser()).parse(jr).getAsJsonObject();

			final JsonObject paths = obj.get("paths").getAsJsonObject();
			for (final Map.Entry<String, JsonElement> path : paths.entrySet()) {
				final String pathUri = path.getKey();
				final String verb = path.getValue().getAsJsonObject().entrySet().iterator().next().getKey();
				parametresMethodeDeTest.add(new Object[] { pathUri, verb });
			}
		}

		return parametresMethodeDeTest.toArray(new Object[][] {});
	}

	@Test(dataProvider = "getTestParameters")
	public void testAccesApiProtege(final String uri, final String verb) {

		// Creation de l'URL
		String url = super.getURL() + uri;

		ResponseEntity<String> reponse = null;
		// post
		if ("post".equals(verb)) {
			url = url.replaceAll("[?,]", "").replaceAll("\\{[^\\}]*\\}$", "").replaceAll("\\{id[a-zA-Z]*\\}", "1");
			reponse = CatchException.catchException(super.getREST()).postForEntity(url, null, String.class);
		}

		// get
		else if ("get".equals(verb)) {
			url = url.replaceAll("\\{id[a-zA-Z]*\\}", "1");
			reponse = CatchException.catchException(super.getREST()).getForEntity(url, String.class);
		}

		// get
		else if ("delete".equals(verb)) {
			url = url.replaceAll("\\{id[a-zA-Z]*\\}", "1").replaceAll("\\{login\\}", "nomUtilisateur");
			reponse = CatchException.catchException(super.getREST()).getForEntity(url, String.class);
		}

		// put
		else if ("put".equals(verb)) {
			url = url.replaceAll("\\{id[a-zA-Z]*\\}", "1").replaceAll("\\{login\\}", "nomUtilisateur");
			;
			CatchException.catchException(super.getREST()).put(url, null);
		}

		// au cs où
		else {
			Assert.fail("cas non traite par ce test ! Une nouvelle URI exposee mais non testee!!");
		}

		// Assert
		final Exception e = CatchException.caughtException();

		// Cas du "dologin" qui n'est pas protegee et donc renvoi 400
		if (url.endsWith("dologin")) {
			Assert.assertNotNull(e);
			Assert.assertEquals(e.getClass(), HttpClientErrorException.class);
			final HttpClientErrorException hcee = (HttpClientErrorException) e;
			Assert.assertEquals(hcee.getStatusCode(), HttpStatus.BAD_REQUEST);
		}

		// Page WEB : redirection quand on a pas de session active
		else if (url.endsWith(".html")) {
			Assert.assertNull(e);
			Assert.assertEquals(reponse.getStatusCode(), HttpStatus.OK);
			Assert.assertTrue(reponse.getBody().contains("divLogin"));
		}

		// Cas des pages protegee : 404 quand on a pas de sessino active
		else {
			Assert.assertNotNull(e);
			Assert.assertEquals(e.getClass(), HttpClientErrorException.class);
			final HttpClientErrorException hcee = (HttpClientErrorException) e;
			Assert.assertEquals(hcee.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
