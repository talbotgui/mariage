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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.rest.controleur.dto.EtapeDTO;
import com.googlecode.catchexception.CatchException;

public class EtapeRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeEtapes() {

		// ARRANGE
		Long idMariage = 10L;
		List<Etape> toReturn = Arrays.asList(new EtapeCeremonie("E1", new Date(), "L1"),
				new EtapeCeremonie("E2", new Date(), "L1"), new EtapeCeremonie("E4", new Date(), "L3"),
				new EtapeCeremonie("E3", new Date(), "L2"), new EtapeCeremonie("E5", new Date(), "L2"));
		Mockito.doReturn(toReturn).when(this.service).listeEtapesParIdMariage(Mockito.anyLong());

		// ACT
		ParameterizedTypeReference<Collection<EtapeDTO>> typeRetour = new ParameterizedTypeReference<Collection<EtapeDTO>>() {
		};
		ResponseEntity<Collection<EtapeDTO>> invites = getREST().exchange(getURL() + "/mariage/" + idMariage + "/etape",
				HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(invites.getBody().size(), 5);
		Mockito.verify(this.service).listeEtapesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02AjouteEtapeCeremonie() {

		// ARRANGE
		Long idMariage = 10L;
		Long idEtape = 100L;
		ArgumentCaptor<Etape> argumentCaptorEtape = ArgumentCaptor.forClass(Etape.class);
		ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idEtape).when(this.service).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorEtape.capture());

		final String nom = "N1";
		final String lieu = "L1";
		final String celebrant = "C1";
		final String dateHeure = "01/01/2017";
		final String type = EtapeCeremonie.class.getSimpleName();
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("lieu", lieu);
		requestParam.add("celebrant", celebrant);
		requestParam.add("dateHeure", dateHeure);
		requestParam.add("type", type);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Long idEtapeRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/etape", requestParam,
				Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idEtapeRetour);
		Assert.assertEquals(idEtapeRetour, idEtape);
		Assert.assertEquals(argumentCaptorEtape.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorEtape.getValue().getLieu(), lieu);
		Assert.assertEquals((new SimpleDateFormat("dd/MM/yyyy")).format(argumentCaptorEtape.getValue().getDateHeure()),
				dateHeure);
		Assert.assertEquals(argumentCaptorEtape.getValue().getClass().getSimpleName(), type);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.service).sauvegarde(Mockito.anyLong(), Mockito.any(Etape.class));
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02AjouteEtapeDateInvalide() {

		// ARRANGE
		Long idMariage = 10L;
		final String nom = "N1";
		final String lieu = "L1";
		final String celebrant = "C1";
		final String dateHeure = "01-01-2017";
		final String type = EtapeRepas.class.getSimpleName();
		;
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("lieu", lieu);
		requestParam.add("celebrant", celebrant);
		requestParam.add("dateHeure", dateHeure);
		requestParam.add("type", type);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/etape",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(dateHeure));
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02AjouteEtapeRepas() {

		// ARRANGE
		Long idMariage = 10L;
		Long idEtape = 100L;
		ArgumentCaptor<Etape> argumentCaptorEtape = ArgumentCaptor.forClass(Etape.class);
		ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idEtape).when(this.service).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorEtape.capture());

		final String nom = "N1";
		final String lieu = "L1";
		final String dateHeure = "01/01/2017";
		final String type = EtapeRepas.class.getSimpleName();
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("lieu", lieu);
		requestParam.add("dateHeure", dateHeure);
		requestParam.add("type", type);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Long idEtapeRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/etape", requestParam,
				Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idEtapeRetour);
		Assert.assertEquals(idEtapeRetour, idEtape);
		Assert.assertEquals(argumentCaptorEtape.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorEtape.getValue().getLieu(), lieu);
		Assert.assertEquals((new SimpleDateFormat("dd/MM/yyyy")).format(argumentCaptorEtape.getValue().getDateHeure()),
				dateHeure);
		Assert.assertEquals(argumentCaptorEtape.getValue().getClass().getSimpleName(), type);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.service).sauvegarde(Mockito.anyLong(), Mockito.any(Etape.class));
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02AjouteEtapeTypeInconu() {

		// ARRANGE
		Long idMariage = 10L;
		final String nom = "N1";
		final String lieu = "L1";
		final String celebrant = "C1";
		final String dateHeure = "01/01/2017";
		final String type = "toto";
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("lieu", lieu);
		requestParam.add("celebrant", celebrant);
		requestParam.add("dateHeure", dateHeure);
		requestParam.add("type", type);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/etape",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(
				((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString().contains(type));
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test03SupprimeEtape() {

		// ARRANGE
		Long idMariage = 10L;
		Long idEtape = 100L;
		ArgumentCaptor<Long> argumentCaptorIdIEtape = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.service).suprimeEtape(argumentCaptorIdMariage.capture(),
				argumentCaptorIdIEtape.capture());

		// ACT
		ResponseEntity<Void> response = getREST().exchange(getURL() + "/mariage/" + idMariage + "/etape/" + idEtape,
				HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdIEtape.getValue(), idEtape);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.service).suprimeEtape(Mockito.anyLong(), Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.service);
	}

}
