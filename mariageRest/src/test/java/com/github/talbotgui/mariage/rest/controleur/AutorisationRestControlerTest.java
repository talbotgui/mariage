package com.github.talbotgui.mariage.rest.controleur;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.rest.controleur.dto.AutorisationDTO;

public class AutorisationRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01ListerAutorisations() {
		final List<Autorisation> toReturn = Arrays.asList(
				new Autorisation(new Mariage(new Date(), "M1", "M12"), new Utilisateur("U1")),
				new Autorisation(new Mariage(new Date(), "M2", "M22"), new Utilisateur("U1")),
				new Autorisation(new Mariage(new Date(), "M3", "M32"), new Utilisateur("U1")),
				new Autorisation(new Mariage(new Date(), "M4", "M42"), new Utilisateur("U1")),
				new Autorisation(new Mariage(new Date(), "M5", "M52"), new Utilisateur("U1")));

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.securiteService).listerAutorisations();

		// ACT
		final ParameterizedTypeReference<Collection<AutorisationDTO>> typeRetour = new ParameterizedTypeReference<Collection<AutorisationDTO>>() {
		};
		final ResponseEntity<Collection<AutorisationDTO>> autorisations = this.getREST()
				.exchange(this.getURL() + "/autorisation", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(autorisations.getBody().size(), 5);
		Mockito.verify(this.securiteService).listerAutorisations();
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}

	@Test
	public void test02AjouteAutorisation() {
		final Long idMariage = 10L;
		final String login = "login1";

		// ARRANGE
		Mockito.doReturn(null).when(this.securiteService).ajouterAutorisation(login, idMariage);

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("login", login,
				"idMariage", idMariage);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		this.getREST().postForObject(this.getURL() + "/autorisation", requestParam, Void.class, uriVars);

		// ASSERT
		Mockito.verify(this.securiteService).ajouterAutorisation(login, idMariage);
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}

	@Test
	public void test03SupprimeAutorisation() {
		final Long idAutorisation = 100L;

		// ARRANGE
		Mockito.doNothing().when(this.securiteService).supprimerAutorisation(idAutorisation);

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(this.getURL() + "/autorisation/" + idAutorisation,
				HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(this.securiteService).supprimerAutorisation(idAutorisation);
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}

}
