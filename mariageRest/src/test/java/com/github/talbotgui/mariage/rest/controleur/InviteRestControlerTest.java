package com.github.talbotgui.mariage.rest.controleur;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.googlecode.catchexception.CatchException;

public class InviteRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeInvites() {

		// ARRANGE
		Long idMariage = 10L;
		List<Invite> toReturn = Arrays.asList(new Invite("G1", "I1", "P1", Age.adulte),
				new Invite("G1", "I2", "P1", Age.adulte), new Invite("G1", "I3", "P1", Age.adulte),
				new Invite("G1", "I4", "P1", Age.adulte), new Invite("G2", "I1", "P1", Age.adulte),
				new Invite("G2", "I2", "P1", Age.adulte), new Invite("G2", "I3", "P1", Age.adulte),
				new Invite("G2", "I4", "P1", Age.adulte));
		Mockito.doReturn(toReturn).when(this.service).listeInvitesParIdMariage(Mockito.anyLong());

		// ACT
		ParameterizedTypeReference<Collection<InviteDTO>> typeRetour = new ParameterizedTypeReference<Collection<InviteDTO>>() {
		};
		ResponseEntity<Collection<InviteDTO>> invites = getREST()
				.exchange(getURL() + "/mariage/" + idMariage + "/invite", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(invites.getBody().size(), 8);
		Mockito.verify(this.service).listeInvitesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02AjouteInvite() {

		// ARRANGE
		Long idMariage = 10L;
		Long idInvite = 100L;
		ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idInvite).when(this.service).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final String nom = "InviteA";
		final String prenom = "BB";
		final String groupe = "Groupe1";
		final String age = Age.adulte.toString();
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("prenom", prenom);
		requestParam.add("groupe", groupe);
		requestParam.add("age", age);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Long idInviteRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/invite", requestParam,
				Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getGroupe(), groupe);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAge().toString(), age);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.service).sauvegarde(Mockito.anyLong(), Mockito.any(Invite.class));
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02AjouteInviteAgeInvalid() {

		// ARRANGE
		Long idMariage = 10L;

		final String nom = "InviteA";
		final String prenom = "BB";
		final String groupe = "Groupe1";
		final String age = "toto";
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("prenom", prenom);
		requestParam.add("groupe", groupe);
		requestParam.add("age", age);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(
				((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString().contains(age));
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test03SupprimeInvite() {

		// ARRANGE
		Long idMariage = 10L;
		Long idInvite = 100L;
		ArgumentCaptor<Long> argumentCaptorIdInvite = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.service).suprimeInvite(argumentCaptorIdMariage.capture(),
				argumentCaptorIdInvite.capture());

		// ACT
		ResponseEntity<Void> response = getREST().exchange(getURL() + "/mariage/" + idMariage + "/invite/" + idInvite,
				HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdInvite.getValue(), idInvite);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.service).suprimeInvite(Mockito.anyLong(), Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.service);
	}

}
