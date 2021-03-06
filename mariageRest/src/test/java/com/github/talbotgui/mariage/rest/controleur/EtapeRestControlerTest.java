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
		final Long idMariage = 10L;
		final List<Etape> toReturn = Arrays.asList(new EtapeCeremonie(1, "E1", new Date(), "L1"),
				new EtapeCeremonie(2, "E2", new Date(), "L1"), new EtapeCeremonie(3, "E4", new Date(), "L3"),
				new EtapeCeremonie(4, "E3", new Date(), "L2"), new EtapeCeremonie(5, "E5", new Date(), "L2"));

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.mariageService).listerEtapesParIdMariage(Mockito.anyLong());

		// ACT
		final ParameterizedTypeReference<Collection<EtapeDTO>> typeRetour = new ParameterizedTypeReference<Collection<EtapeDTO>>() {
		};
		final ResponseEntity<Collection<EtapeDTO>> invites = getREST()
				.exchange(getURL() + "/mariage/" + idMariage + "/etape", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(invites.getBody().size(), 5);
		Mockito.verify(this.mariageService).listerEtapesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteEtape01Ceremonie() {
		final Long idMariage = 10L;
		final Long idEtape = 100L;
		final String numOrdre = "1";
		final String nom = "N1";
		final String lieu = "L1";
		final String celebrant = "C1";
		final String dateHeure = "01/01/2017 12:00";
		final String type = EtapeCeremonie.class.getSimpleName();

		// ARRANGE
		final ArgumentCaptor<Etape> argumentCaptorEtape = ArgumentCaptor.forClass(Etape.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idEtape).when(this.mariageService).sauvegarder(argumentCaptorIdMariage.capture(),
				argumentCaptorEtape.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom, "lieu", lieu,
				"celebrant", celebrant, "dateHeure", dateHeure, "type", type, "numOrdre", numOrdre);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idEtapeRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/etape", requestParam,
				Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idEtapeRetour);
		Assert.assertEquals(idEtapeRetour, idEtape);
		Assert.assertEquals(argumentCaptorEtape.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorEtape.getValue().getLieu(), lieu);
		Assert.assertEquals(
				(new SimpleDateFormat("dd/MM/yyyy hh:mm")).format(argumentCaptorEtape.getValue().getDateHeure()),
				dateHeure);
		Assert.assertEquals(argumentCaptorEtape.getValue().getClass().getSimpleName(), type);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarder(Mockito.anyLong(), Mockito.any(Etape.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteEtape02Repas() {
		final Long idMariage = 10L;
		final Long idEtape = 100L;
		final String nom = "N1";
		final String lieu = "L1";
		final String dateHeure = "01/01/2017 12:00";
		final String type = EtapeRepas.class.getSimpleName();

		// ARRANGE
		final ArgumentCaptor<Etape> argumentCaptorEtape = ArgumentCaptor.forClass(Etape.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idEtape).when(this.mariageService).sauvegarder(argumentCaptorIdMariage.capture(),
				argumentCaptorEtape.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom, "lieu", lieu,
				"dateHeure", dateHeure, "type", type);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idEtapeRetour = getREST().postForObject(getURL() + "/mariage/" + idMariage + "/etape", requestParam,
				Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idEtapeRetour);
		Assert.assertEquals(idEtapeRetour, idEtape);
		Assert.assertEquals(argumentCaptorEtape.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorEtape.getValue().getLieu(), lieu);
		Assert.assertEquals(
				(new SimpleDateFormat("dd/MM/yyyy hh:mm")).format(argumentCaptorEtape.getValue().getDateHeure()),
				dateHeure);
		Assert.assertEquals(argumentCaptorEtape.getValue().getClass().getSimpleName(), type);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarder(Mockito.anyLong(), Mockito.any(Etape.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteEtape03DateInvalide() {
		final Long idMariage = 10L;
		final String nom = "N1";
		final String lieu = "L1";
		final String celebrant = "C1";
		final String dateHeure = "01-01-2017 12:00";
		final String type = EtapeRepas.class.getSimpleName();

		// ARRANGE
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom, "lieu", lieu,
				"celebrant", celebrant, "dateHeure", dateHeure, "type", type);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/etape",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(dateHeure));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteEtape04TypeInconu() {
		final Long idMariage = 10L;
		final String numOrdre = "1";
		final String nom = "N1";
		final String lieu = "L1";
		final String celebrant = "C1";
		final String dateHeure = "01/01/2017 12:00";
		final String type = "toto";

		// ARRANGE
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom, "lieu", lieu,
				"celebrant", celebrant, "dateHeure", dateHeure, "type", type, "numOrdre", numOrdre);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/etape",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(
				((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString().contains(type));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteEtape05NumOrdreNonNumerique() {
		final Long idMariage = 10L;
		final String numOrdre = "toto";
		final String nom = "N1";
		final String lieu = "L1";
		final String celebrant = "C1";
		final String dateHeure = "01/01/2017 12:00";
		final String type = EtapeRepas.class.getSimpleName();

		// ARRANGE
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom, "lieu", lieu,
				"celebrant", celebrant, "dateHeure", dateHeure, "type", type, "numOrdre", numOrdre);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage/" + idMariage + "/etape",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(numOrdre));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test03SupprimeEtape() {
		final Long idMariage = 10L;
		final Long idEtape = 100L;

		// ARRANGE
		final ArgumentCaptor<Long> argumentCaptorIdIEtape = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.mariageService).supprimerEtape(argumentCaptorIdMariage.capture(),
				argumentCaptorIdIEtape.capture());

		// ACT
		final ResponseEntity<Void> response = getREST().exchange(
				getURL() + "/mariage/" + idMariage + "/etape/" + idEtape, HttpMethod.DELETE, null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdIEtape.getValue(), idEtape);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).supprimerEtape(Mockito.anyLong(), Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

}
