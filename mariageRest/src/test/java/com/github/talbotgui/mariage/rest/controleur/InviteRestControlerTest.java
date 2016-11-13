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
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Presence;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.PresenceDTO;
import com.googlecode.catchexception.CatchException;

public class InviteRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeInvites() {
		final Long idMariage = 10L;
		final List<Invite> toReturn = Arrays.asList(new Invite("I1", "P1", Age.adulte),
				new Invite("I2", "P1", Age.adulte), new Invite("I3", "P1", Age.adulte),
				new Invite("I4", "P1", Age.adulte), new Invite("I1", "P1", Age.adulte),
				new Invite("I2", "P1", Age.adulte), new Invite("I3", "P1", Age.adulte),
				new Invite("I4", "P1", Age.adulte));

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.mariageService).listeInvitesParIdMariage(Mockito.anyLong());

		// ACT
		final ParameterizedTypeReference<Collection<InviteDTO>> typeRetour = new ParameterizedTypeReference<Collection<InviteDTO>>() {
		};
		final ResponseEntity<Collection<InviteDTO>> invites = this.getREST()
				.exchange(this.getURL() + "/mariage/" + idMariage + "/invite", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(invites.getBody().size(), 8);
		Mockito.verify(this.mariageService).listeInvitesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteInvite01FoyerInexistant() {
		final Long idMariage = 10L;
		final Long idInvite = 100L;
		final String age = Age.adulte.toString();
		final String foyer = "foyer";
		final String groupe = "Groupe1";
		final String nom = "InviteA";
		final String prenom = "BB";

		// ARRANGE
		final ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(null).when(this.mariageService).getFoyer(Mockito.anyLong(), Mockito.anyString());
		Mockito.doReturn(idInvite).when(this.mariageService).sauvegardeInviteEtFoyer(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("age", age, "foyer",
				foyer, "groupe", groupe, "nom", nom, "prenom", prenom);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idInviteRetour = this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAge().toString(), age);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getNom(), foyer);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getGroupe(), groupe);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom);
		Assert.assertNull(argumentCaptorInvite.getValue().getFoyer().getTelephone());
		Assert.assertNull(argumentCaptorInvite.getValue().getFoyer().getAdresse());
		Assert.assertNull(argumentCaptorInvite.getValue().getFoyer().getEmail());
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).getFoyer(idMariage, foyer);
		Mockito.verify(this.mariageService).sauvegardeInviteEtFoyer(Mockito.anyLong(), Mockito.any(Invite.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteInvite02FoyerExistant() {
		final Long idMariage = 10L;
		final Long idInvite = 100L;
		final Long idFoyer = 50L;
		final String age = Age.adulte.toString();
		final String foyer = "foyer";
		final String groupe1 = "Groupe1";
		final String groupe2 = "Groupe2";
		final String nom = "InviteA";
		final String prenom = "BB";
		final String adresse = "add";
		final String email = "email";
		final String telephone = "telephone";

		// ARRANGE
		final ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(new Foyer(idFoyer, groupe1, foyer, adresse, email, telephone)).when(this.mariageService)
				.getFoyer(Mockito.anyLong(), Mockito.anyString());
		Mockito.doReturn(idInvite).when(this.mariageService).sauvegardeInviteEtFoyer(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("age", age, "foyer",
				foyer, "groupe", groupe2, "nom", nom, "prenom", prenom);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idInviteRetour = this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAge().toString(), age);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getNom(), foyer);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getGroupe(), groupe2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getTelephone(), telephone);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getAdresse(), adresse);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getEmail(), email);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).getFoyer(idMariage, foyer);
		Mockito.verify(this.mariageService).sauvegardeInviteEtFoyer(Mockito.anyLong(), Mockito.any(Invite.class));
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
		CatchException.catchException(this.getREST()).postForObject(this.getURL() + "/mariage/" + idMariage + "/invite",
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
		CatchException.catchException(this.getREST()).postForObject(this.getURL() + "/mariage/" + idMariage + "/invite",
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
		Mockito.doNothing().when(this.mariageService).supprimeInvite(argumentCaptorIdMariage.capture(),
				argumentCaptorIdInvite.capture());

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(
				this.getURL() + "/mariage/" + idMariage + "/invite/" + idInvite, HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdInvite.getValue(), idInvite);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).supprimeInvite(Mockito.anyLong(), Mockito.anyLong());
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
		final StatistiquesMariage resultats = this.getREST()
				.getForObject(this.getURL() + "/mariage/" + idMariage + "/statistiques", StatistiquesMariage.class);

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
		final String groupe = "Groupe";
		final String groupe2 = "Groupe2";
		final String nom = "InviteA";
		final String nom2 = "InviteB";
		final String prenom = "BB";
		final String prenom2 = "CC";
		final String telephone = "telephone";
		final String telephone2 = "telephone2";

		// ARRANGE
		final Invite invite = new Invite(idInvite, nom, prenom, age,
				new Foyer(groupe, foyer, adresse, email, telephone));
		final ArgumentCaptor<Long> argumentCaptorIdInvite = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(invite).when(this.mariageService).chargeInviteParId(argumentCaptorIdInvite.capture());

		final ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idInvite).when(this.mariageService).sauvegardeInviteEtFoyer(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("id", idInvite, "email",
				email2, "nom", nom2, "prenom", prenom2, "telephone", telephone2, "groupe", groupe2, "foyer", foyer2,
				"age", age2.toString(), "adresse", adresse2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idInviteRetour = this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);

		Assert.assertEquals(argumentCaptorInvite.getValue().getAge(), age2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getNom(), foyer2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getGroupe(), groupe2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom2);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom2);

		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getAdresse(), adresse);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getTelephone(), telephone);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getEmail(), email);

		Mockito.verify(this.mariageService).chargeInviteParId(idInvite);
		Mockito.verify(this.mariageService).sauvegardeInviteEtFoyer(Mockito.eq(idMariage), Mockito.any(Invite.class));
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
		final Invite invite = new Invite(idInvite, nom, prenom, age,
				new Foyer(groupe, foyer, adresse, email, telephone));
		final ArgumentCaptor<Long> argumentCaptorIdInvite = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(invite).when(this.mariageService).chargeInviteParId(argumentCaptorIdInvite.capture());

		final ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idInvite).when(this.mariageService).sauvegardeInviteEtFoyer(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("id", idInvite, "email",
				email2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idInviteRetour = this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/invite",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInviteRetour, idInvite);
		Assert.assertEquals(argumentCaptorInvite.getValue().getAge(), age);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getNom(), foyer);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getGroupe(), groupe);
		Assert.assertEquals(argumentCaptorInvite.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorInvite.getValue().getPrenom(), prenom);

		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getAdresse(), adresse);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getTelephone(), telephone);
		Assert.assertEquals(argumentCaptorInvite.getValue().getFoyer().getEmail(), email);

		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).chargeInviteParId(idInvite);
		Mockito.verify(this.mariageService).sauvegardeInviteEtFoyer(Mockito.eq(idMariage), Mockito.any(Invite.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test07ListePresenceParIdMariage() {
		final Long idMariage = 10L;
		final Etape etape1 = new EtapeRepas(1L);
		final Etape etape2 = new EtapeRepas(2L);
		final Invite invite1 = new Invite(1L);
		final Invite invite2 = new Invite(2L);
		final Invite invite3 = new Invite(3L);
		final List<Presence> toReturn = Arrays.asList(new Presence(etape1, invite1), new Presence(etape2, invite1),
				new Presence(etape1, invite2), new Presence(etape2, invite2), new Presence(etape1, invite3),
				new Presence(etape2, invite3));

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.mariageService).listePresencesParIdMariage(Mockito.anyLong());

		// ACT
		final ParameterizedTypeReference<Collection<PresenceDTO>> typeRetour = new ParameterizedTypeReference<Collection<PresenceDTO>>() {
		};
		final ResponseEntity<Collection<PresenceDTO>> invites = this.getREST()
				.exchange(this.getURL() + "/mariage/" + idMariage + "/presence", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(invites.getBody().size(), 6);
		Mockito.verify(this.mariageService).listePresencesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test08ModificationPresence01Existant() {
		final Long idMariage = 10L;
		final Long idEtape = 1L;
		final Long idInvite = 2L;
		final String commentaire = "commentaire";
		final Boolean confirmee = true;
		final Boolean present = true;

		// ARRANGE
		final Presence presence = new Presence(new EtapeRepas(idEtape), new Invite(idInvite));
		Mockito.doReturn(presence).when(this.mariageService).chargePresenceParEtapeEtInvite(Mockito.anyLong(),
				Mockito.anyLong(), Mockito.anyLong());

		final ArgumentCaptor<Presence> argumentCaptorPresence = ArgumentCaptor.forClass(Presence.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage2 = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.mariageService).sauvegarde(argumentCaptorIdMariage2.capture(),
				argumentCaptorPresence.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("idEtape", idEtape,
				"idInvite", idInvite, "commentaire", commentaire, "confirmee", confirmee, "present", present);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/presence", requestParam, Void.class,
				uriVars);

		// ASSERT
		Assert.assertEquals(argumentCaptorIdMariage2.getValue(), idMariage);
		Assert.assertEquals(argumentCaptorPresence.getValue().getCommentaire(), commentaire);
		Assert.assertEquals(argumentCaptorPresence.getValue().getConfirmee(), confirmee);
		Assert.assertEquals(argumentCaptorPresence.getValue().getPresent(), present);
		Assert.assertEquals(argumentCaptorPresence.getValue().getId().getEtape().getId(), idEtape);
		Assert.assertEquals(argumentCaptorPresence.getValue().getId().getInvite().getId(), idInvite);

		Mockito.verify(this.mariageService).chargePresenceParEtapeEtInvite(idMariage, idEtape, idInvite);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.eq(idMariage), Mockito.any(Presence.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test08ModificationPresence02NonExistant() {
		final Long idMariage = 10L;
		final Long idEtape = 1L;
		final Long idInvite = 2L;
		final String commentaire = "commentaire";
		final Boolean confirmee = true;
		final Boolean present = true;

		// ARRANGE
		Mockito.doReturn(null).when(this.mariageService).chargePresenceParEtapeEtInvite(Mockito.anyLong(),
				Mockito.anyLong(), Mockito.anyLong());

		final ArgumentCaptor<Presence> argumentCaptorPresence = ArgumentCaptor.forClass(Presence.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage2 = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.mariageService).sauvegarde(argumentCaptorIdMariage2.capture(),
				argumentCaptorPresence.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("idEtape", idEtape,
				"idInvite", idInvite, "commentaire", commentaire, "confirmee", confirmee, "present", present);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/presence", requestParam, Void.class,
				uriVars);

		// ASSERT
		Assert.assertEquals(argumentCaptorIdMariage2.getValue(), idMariage);
		Assert.assertEquals(argumentCaptorPresence.getValue().getCommentaire(), commentaire);
		Assert.assertEquals(argumentCaptorPresence.getValue().getConfirmee(), confirmee);
		Assert.assertEquals(argumentCaptorPresence.getValue().getPresent(), present);
		Assert.assertEquals(argumentCaptorPresence.getValue().getId().getEtape().getId(), idEtape);
		Assert.assertEquals(argumentCaptorPresence.getValue().getId().getInvite().getId(), idInvite);

		Mockito.verify(this.mariageService).chargePresenceParEtapeEtInvite(idMariage, idEtape, idInvite);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.eq(idMariage), Mockito.any(Presence.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test09SupprimePresence() {
		final Long idMariage = 10L;
		final Long idEtape = 1L;
		final Long idInvite = 2L;

		// ARRANGE
		Mockito.doNothing().when(this.mariageService).supprimePresence(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.anyLong());

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(
				this.getURL() + "/mariage/" + idMariage + "/presence?idEtape=" + idEtape + "&idInvite=" + idInvite,
				HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(this.mariageService).supprimePresence(idMariage, idInvite, idEtape);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

}
