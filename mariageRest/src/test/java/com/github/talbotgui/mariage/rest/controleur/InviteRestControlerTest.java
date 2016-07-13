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
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesRepartitionsInvitesMariage;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.googlecode.catchexception.CatchException;

public class InviteRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeInvites() {
		final Long idMariage = 10L;
		final List<Invite> toReturn = Arrays.asList(new Invite("G1", "I1", "P1", Age.adulte),
				new Invite("G1", "I2", "P1", Age.adulte), new Invite("G1", "I3", "P1", Age.adulte),
				new Invite("G1", "I4", "P1", Age.adulte), new Invite("G2", "I1", "P1", Age.adulte),
				new Invite("G2", "I2", "P1", Age.adulte), new Invite("G2", "I3", "P1", Age.adulte),
				new Invite("G2", "I4", "P1", Age.adulte));

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.mariageService).listeInvitesParIdMariage(Mockito.anyLong());

		// ACT
		final ParameterizedTypeReference<Collection<InviteDTO>> typeRetour = new ParameterizedTypeReference<Collection<InviteDTO>>() {
		};
		final ResponseEntity<Collection<InviteDTO>> invites = getREST()
				.exchange(getURL() + "/mariage/" + idMariage + "/invite", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(invites.getBody().size(), 8);
		Mockito.verify(this.mariageService).listeInvitesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteInvite() {
		final Long idMariage = 10L;
		final Long idInvite = 100L;
		final String adresse = "adresse";
		final String email = "email";
		final String age = Age.adulte.toString();
		final String foyer = "foyer";
		final String groupe = "Groupe1";
		final String nom = "InviteA";
		final String prenom = "BB";
		final String telephone = "telephone";

		// ARRANGE
		final ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idInvite).when(this.mariageService).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("adresse", adresse,
				"email", email, "age", age, "foyer", foyer, "groupe", groupe, "nom", nom, "prenom", prenom, "telephone",
				telephone);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idInviteRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAdresse(), adresse);
		Assert.assertEquals(argumentCaptorInvite.getValue().getEmail(), email);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAge().toString(), age);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer(), foyer);
		Assert.assertEquals(argumentCaptorInvite.getValue().getGroupe(), groupe);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getTelephone(), telephone);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.anyLong(), Mockito.any(Invite.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteInviteAgeInvalid() {
		final Long idMariage = 10L;
		final String adresse = "adresse";
		final String age = "toto";
		final String foyer = "foyer";
		final String groupe = "Groupe1";
		final String nom = "InviteA";
		final String prenom = "BB";
		final String telephone = "telephone";

		// ARRANGE
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("adresse", adresse, "age",
				age, "foyer", foyer, "groupe", groupe, "nom", nom, "prenom", prenom, "telephone", telephone);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(
				((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString().contains(age));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteInviteValeurObligatoireVide() {
		final Long idMariage = 10L;
		final String adresse = "adresse";
		final String age = "toto";
		final String foyer = "foyer";
		final String groupe = "Groupe1";
		final String nom = "";
		final String prenom = "BB";
		final String telephone = "telephone";

		// ARRANGE
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("adresse", adresse, "age",
				age, "foyer", foyer, "groupe", groupe, "nom", nom, "prenom", prenom, "telephone", telephone);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test03SupprimeInvite() {
		final Long idMariage = 10L;
		final Long idInvite = 100L;

		// ARRANGE
		final ArgumentCaptor<Long> argumentCaptorIdInvite = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.mariageService).suprimeInvite(argumentCaptorIdMariage.capture(),
				argumentCaptorIdInvite.capture());

		// ACT
		final ResponseEntity<Void> response = getREST().exchange(
				getURL() + "/mariage/" + idMariage + "/invite/" + idInvite, HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdInvite.getValue(), idInvite);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).suprimeInvite(Mockito.anyLong(), Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test04AjouteInviteEnMasse() {
		final Long idMariage = 10L;
		final String nom = "Nom1";
		final String prenom = "Prenom1";
		final String groupe = "Groupe1";
		final String adresse = "Adresse1";
		final String invites = nom + ":" + prenom + ":" + groupe + ":" + adresse
				+ "\nNom3:Prenom1:Group3;Adresse3\n\nNom2:Prenom2:Group2;Adresse2\nNom4:Prenom4:Group4;Adresse4";

		// ARRANGE
		final ArgumentCaptor<Collection> argumentCaptorInvites = ArgumentCaptor.forClass(Collection.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.mariageService).sauvegardeEnMasse(argumentCaptorIdMariage.capture(),
				argumentCaptorInvites.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("invites", invites);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		getREST().postForObject(getURL() + "/mariage/" + idMariage + "/inviteEnMasse", requestParam, Void.class,
				uriVars);

		// ASSERT
		Assert.assertEquals(argumentCaptorInvites.getValue().size(), 4);
		for (final Invite i : (Collection<Invite>) argumentCaptorInvites.getValue()) {
			Assert.assertTrue(i.getNom() != null && i.getNom().length() > 0);
			Assert.assertTrue(i.getPrenom() != null && i.getPrenom().length() > 0);
			Assert.assertTrue(i.getGroupe() != null && i.getGroupe().length() > 0);
			Assert.assertTrue(i.getAdresse() != null && i.getAdresse().length() > 0);
		}
		final Invite invite1 = (Invite) argumentCaptorInvites.getValue().iterator().next();
		Assert.assertEquals(invite1.getNom(), nom);
		Assert.assertEquals(invite1.getPrenom(), prenom);
		Assert.assertEquals(invite1.getGroupe(), groupe);
		Assert.assertEquals(invite1.getAdresse(), adresse);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegardeEnMasse(Mockito.anyLong(), Mockito.any(Collection.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test05CalculStatistiques() {
		final Long idMariage = 10L;

		// ARRANGE
		final StatistiquesMariage dto = new StatistiquesMariage(
				new StatistiquesRepartitionsInvitesMariage(null, null, null, null, null),
				new StatistiquesInvitesMariage(0L, 0L, 0, 0, 0, 0));
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(dto).when(this.mariageService).calculStatistiques(argumentCaptorIdMariage.capture());

		// ACT
		final StatistiquesMariage resultats = getREST()
				.getForObject(getURL() + "/mariage/" + idMariage + "/statistiques", StatistiquesMariage.class);

		// ASSERT
		Assert.assertNotNull(resultats);
		Assert.assertNotNull(resultats.getInvites());
		Assert.assertNotNull(resultats.getRepartitions());
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).calculStatistiques(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test06ModificationInvitePlusieursChamps() {
		final Long idMariage = 10L;
		final Long idInvite = 100L;
		final String adresse = "adresse";
		final String adresse2 = "adresse2";
		final String email = "email";
		final String email2 = "email2";
		final Age age = Age.adulte;
		final Age age2 = Age.aine;
		final String foyer = "foyer";
		final String foyer2 = "foyer2";
		final String groupe = "Groupe1";
		final String groupe2 = "Groupe2";
		final String nom = "InviteA";
		final String nom2 = "InviteB";
		final String prenom = "BB";
		final String prenom2 = "CC";
		final String telephone = "telephone";
		final String telephone2 = "telephone2";

		// ARRANGE
		final Invite invite = new Invite(idInvite, nom, prenom, groupe, foyer, age, adresse, telephone, email);
		final ArgumentCaptor<Long> argumentCaptorIdInvite = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(invite).when(this.mariageService).chargeInviteParId(argumentCaptorIdInvite.capture());

		final ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idInvite).when(this.mariageService).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("id", idInvite, "email",
				email2, "nom", nom2, "prenom", prenom2, "telephone", telephone2, "groupe", groupe2, "foyer", foyer2,
				"age", age2.toString(), "adresse", adresse2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idInviteRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAdresse(), adresse2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAge(), age2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer(), foyer2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getGroupe(), groupe2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getTelephone(), telephone2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getEmail(), email2);
		Mockito.verify(this.mariageService).chargeInviteParId(idInvite);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.eq(idMariage), Mockito.any(Invite.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test06ModificationInviteUnSeulChamp() {
		final Long idMariage = 10L;
		final Long idInvite = 100L;
		final String adresse = "adresse";
		final String email = "email";
		final String email2 = "email2";
		final Age age = Age.adulte;
		final String foyer = "foyer";
		final String groupe = "Groupe1";
		final String nom = "InviteA";
		final String prenom = "BB";
		final String telephone = "telephone";

		// ARRANGE
		final Invite invite = new Invite(idInvite, nom, prenom, groupe, foyer, age, adresse, telephone, email);
		final ArgumentCaptor<Long> argumentCaptorIdInvite = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(invite).when(this.mariageService).chargeInviteParId(argumentCaptorIdInvite.capture());

		final ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idInvite).when(this.mariageService).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("id", idInvite, "email",
				email2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idInviteRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAdresse(), adresse);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAge(), age);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer(), foyer);
		Assert.assertEquals(argumentCaptorInvite.getValue().getGroupe(), groupe);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getTelephone(), telephone);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Assert.assertEquals(argumentCaptorInvite.getValue().getEmail(), email2);
		Mockito.verify(this.mariageService).chargeInviteParId(idInvite);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.eq(idMariage), Mockito.any(Invite.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

}
