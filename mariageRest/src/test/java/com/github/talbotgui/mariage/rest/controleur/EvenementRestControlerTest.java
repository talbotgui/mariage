package com.github.talbotgui.mariage.rest.controleur;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.Evenement;
import com.github.talbotgui.mariage.rest.controleur.dto.EvenementDTO;

public class EvenementRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01ListerEvenements() {
		final Long idMariage = 10L;
		final List<Evenement> toReturn = Arrays.asList(new Evenement("T1", new Date(), new Date()),
				new Evenement("T2", new Date(), new Date()), new Evenement("T3", new Date(), new Date()),
				new Evenement("T4", new Date(), new Date()));

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.mariageService).listerEvenementsParIdMariage(Mockito.anyLong());

		// ACT
		final ParameterizedTypeReference<Collection<EvenementDTO>> typeRetour = new ParameterizedTypeReference<Collection<EvenementDTO>>() {
		};
		final ResponseEntity<Collection<EvenementDTO>> evenements = this.getREST()
				.exchange(this.getURL() + "/mariage/" + idMariage + "/evenement", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(evenements.getBody().size(), 4);
		Mockito.verify(this.mariageService).listerEvenementsParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteEvenement() {
		final Long idMariage = 10L;
		final Long idEvenement = 100L;
		final String titre = "N1";
		final String dateDebut = "01/01/2017 12:00";
		final String dateFin = "01/01/2017 14:00";
		final String participants = "1,2,3";

		// ARRANGE
		final ArgumentCaptor<Evenement> argumentCaptorEvenement = ArgumentCaptor.forClass(Evenement.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idEvenement).when(this.mariageService).sauvegarder(argumentCaptorIdMariage.capture(),
				argumentCaptorEvenement.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("titre", titre, "debut",
				dateDebut, "fin", dateFin, "participants", participants);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idEvenementRetour = this.getREST().postForObject(
				this.getURL() + "/mariage/" + idMariage + "/evenement", requestParam, Long.class, uriVars);

		// ASSERT
		final SimpleDateFormat sdf = (new SimpleDateFormat("dd/MM/yyyy HH:mm"));
		Assert.assertNotNull(idEvenementRetour);
		Assert.assertEquals(idEvenementRetour, idEvenement);
		Assert.assertEquals(argumentCaptorEvenement.getValue().getTitre(), titre);
		Assert.assertEquals(sdf.format(argumentCaptorEvenement.getValue().getDebut()), dateDebut);
		Assert.assertEquals(sdf.format(argumentCaptorEvenement.getValue().getFin()), dateFin);
		Assert.assertEquals(argumentCaptorEvenement.getValue().getParticipants().size(), 3);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarder(Mockito.anyLong(), Mockito.any(Evenement.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test03SupprimeEvenement() {
		final Long idMariage = 10L;
		final Long idEvenement = 100L;

		// ARRANGE
		Mockito.doNothing().when(this.mariageService).supprimerEvenement(Mockito.anyLong(), Mockito.anyLong());

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(
				this.getURL() + "/mariage/" + idMariage + "/evenement/" + idEvenement, HttpMethod.DELETE, null,
				Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(this.mariageService).supprimerEvenement(idMariage, idEvenement);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

}
