package com.github.talbotgui.mariage.rest.controleur;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.github.talbotgui.mariage.rest.controleur.dto.UtilisateurDTO;
import com.googlecode.catchexception.CatchException;

public class UtilisateurRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeUtilisateur() {
		final List<Utilisateur> toReturn = Arrays.asList(new Utilisateur("l1", "m1"), new Utilisateur("l2", "m2"),
				new Utilisateur("l3", "m3"));

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.securiteService).listerUtilisateurs();

		// ACT
		final ParameterizedTypeReference<Collection<UtilisateurDTO>> typeRetour = new ParameterizedTypeReference<Collection<UtilisateurDTO>>() {
		};
		final ResponseEntity<Collection<UtilisateurDTO>> utilisateurs = this.getREST()
				.exchange(this.getURL() + "/utilisateur", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertNotNull(utilisateurs.getBody());
		Assert.assertEquals(utilisateurs.getBody().size(), toReturn.size());
		Assert.assertEquals(utilisateurs.getBody().iterator().next().getLogin(), toReturn.iterator().next().getLogin());
		Mockito.verify(this.securiteService).listerUtilisateurs();
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}

	@Test
	public void test02SauvegardeUtilisateur() {
		final String login = "monLogin";
		final String mdp = "monMdp";
		final Utilisateur.Role role = Utilisateur.Role.UTILISATEUR;

		// ARRANGE
		Mockito.doNothing().when(this.securiteService).sauvegarderUtilisateur(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyObject());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("login", login, "mdp",
				mdp, "role", role.toString());
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		this.getREST().postForObject(this.getURL() + "/utilisateur", requestParam, Void.class, uriVars);

		// ASSERT
		Mockito.verify(this.securiteService).sauvegarderUtilisateur(login, mdp, role);
		Mockito.verifyNoMoreInteractions(this.securiteService);

	}

	@Test
	public void test03SupprimeUtilisateur() {
		final String login = "monLogin";

		// ARRANGE
		final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.doNothing().when(this.securiteService).supprimerUtilisateur(argumentCaptor.capture());

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(this.getURL() + "/utilisateur/" + login,
				HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptor.getValue(), login);
		Mockito.verify(this.securiteService).supprimerUtilisateur(Mockito.anyString());
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}

	@Test
	public void test04Login01Ok() {
		final String login = "monLogin";
		final String mdp = "monMdp";

		// ARRANGE
		Mockito.doReturn(Role.ADMIN).when(this.securiteService).verifierUtilisateur(Mockito.anyString(),
				Mockito.anyString());

		// ACT
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("login", login, "mdp",
				mdp);
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", "idMariage=4");
		final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(requestParam, headers);
		final ResponseEntity<Void> response = this.getREST().exchange(this.getURL() + "/dologin", HttpMethod.POST,
				request, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(response.getHeaders().get("Cookie"), null);
		Mockito.verify(this.securiteService).verifierUtilisateur(login, mdp);
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}

	@Test
	public void test04Login02Ko() {
		final String login = "monLogin";
		final String mdp = "monMdp";

		// ARRANGE
		Mockito.doThrow(new BusinessException(BusinessException.ERREUR_LOGIN)).when(this.securiteService)
				.verifierUtilisateur(Mockito.anyString(), Mockito.anyString());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("login", login, "mdp",
				mdp);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(this.getREST()).postForObject(this.getURL() + "/dologin", requestParam,
				Void.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertEquals(CatchException.caughtException().getClass(), HttpClientErrorException.class);
		Assert.assertEquals(HttpStatus.FORBIDDEN,
				((HttpClientErrorException) CatchException.caughtException()).getStatusCode());
		Mockito.verify(this.securiteService).verifierUtilisateur(login, mdp);
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}

	@Test
	public void test05Logout() {

		// ARRANGE

		// ACT
		this.getREST().getForObject(this.getURL() + "/dologout", Void.class);

		// ASSERT
		// Nothing to do
	}

	@Test
	public void test06SauvegardeUtilisateurKo() {
		final String login = "mon.Login";
		final String mdp = "monMdp";
		final Role role = Role.ADMIN;

		// ARRANGE
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("login", login, "mdp",
				mdp, "role", role);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(this.getREST()).postForObject(this.getURL() + "/utilisateur", requestParam,
				Void.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString()
				.contains("login"));
		Mockito.verifyNoMoreInteractions(this.securiteService);

	}

	@Test
	public void test07DeverrouilleUtilisateur() {
		final String login = "mon.Login";

		// ARRANGE
		Mockito.doNothing().when(this.securiteService).deverrouillerUtilisateur(Mockito.anyString());

		// ACT
		this.getREST().put(this.getURL() + "/utilisateur/" + login + "/deverrouille", null);

		// ASSERT
		Mockito.verify(this.securiteService).deverrouillerUtilisateur(login);
		Mockito.verifyNoMoreInteractions(this.securiteService);

	}

	@Test
	public void test08GetUtilisateurMoi() {
		final Utilisateur u = new Utilisateur("monLogin", "mdp");

		// ARRANGE
		Mockito.doReturn(u).when(this.securiteService).chargerUtilisateur(null);

		// ACT
		final UtilisateurDTO uDto = this.getREST().getForObject(this.getURL() + "/utilisateur/moi",
				UtilisateurDTO.class);

		// ASSERT
		Assert.assertEquals(uDto.getLogin(), u.getLogin());
		Assert.assertEquals(uDto.getRole(), u.getRole().name());
		Mockito.verify(this.securiteService).chargerUtilisateur(null);
		Mockito.verifyNoMoreInteractions(this.securiteService);

	}

	@Test
	public void test99BugSuppressionUtilisateurDontLoginContientUnPoint() {
		final String login = "mon.login";
		final String loginObtenu = "mon.login".substring(0, login.indexOf("."));

		// ARRANGE
		final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.doNothing().when(this.securiteService).supprimerUtilisateur(argumentCaptor.capture());

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(this.getURL() + "/utilisateur/" + login,
				HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptor.getValue(), loginObtenu);
		Mockito.verify(this.securiteService).supprimerUtilisateur(Mockito.anyString());
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}
}
