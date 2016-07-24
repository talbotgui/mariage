package com.github.talbotgui.mariage.rest.controleur;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.rest.controleur.dto.ReponseAvecChoix;
import com.googlecode.catchexception.CatchException;

public class CourrierRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeCourriers() {
		final Long idMariage = 10L;
		final List<Courrier> courrierToReturn = Arrays.asList(new Courrier(1L, "C1", new Date()),
				new Courrier(2L, "C2", new Date()), new Courrier(3L, "C4", new Date()),
				new Courrier(4L, "C3", new Date()), new Courrier(5L, "C5", new Date()));
		final List<Etape> etapesToReturn = Arrays.asList(new EtapeCeremonie(1, "etape1", new Date(), "lieu1"),
				new EtapeCeremonie(2, "etape2", new Date(), "lieu2"), new EtapeRepas(3, "etape3", new Date(), "lieu3"),
				new EtapeRepas(3, "etape3", new Date(), "lieu3"));

		// ARRANGE
		Mockito.doReturn(courrierToReturn).when(this.mariageService).listeCourriersParIdMariage(Mockito.anyLong());
		Mockito.doReturn(etapesToReturn).when(this.mariageService).listeEtapesParIdMariage(Mockito.anyLong());

		// ACT
		final ParameterizedTypeReference<ReponseAvecChoix> typeRetour = new ParameterizedTypeReference<ReponseAvecChoix>() {
		};
		final ResponseEntity<ReponseAvecChoix> courriers = this.getREST()
				.exchange(this.getURL() + "/mariage/" + idMariage + "/courrier", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(courriers.getBody().getChoixPossibles().size(), 4);
		Assert.assertEquals(courriers.getBody().getDtos().size(), 5);
		Mockito.verify(this.mariageService).listeCourriersParIdMariage(idMariage);
		Mockito.verify(this.mariageService).listeEtapesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteCourrier01Ok() {
		final Long idMariage = 10L;
		final Long idCourrier = 100L;
		final String nom = "N1";
		final String datePrevisionEnvoi = "01/01/2017";

		// ARRANGE
		final ArgumentCaptor<Courrier> argumentCaptorCourrier = ArgumentCaptor.forClass(Courrier.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idCourrier).when(this.mariageService).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorCourrier.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom,
				"datePrevisionEnvoi", datePrevisionEnvoi);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idCourrierRetour = this.getREST().postForObject(
				this.getURL() + "/mariage/" + idMariage + "/courrier", requestParam, Long.class, uriVars);

		// ASSERT
		final SimpleDateFormat sdf = (new SimpleDateFormat("dd/MM/yyyy"));
		Assert.assertNotNull(idCourrierRetour);
		Assert.assertEquals(idCourrierRetour, idCourrier);
		Assert.assertEquals(argumentCaptorCourrier.getValue().getNom(), nom);
		Assert.assertEquals(sdf.format(argumentCaptorCourrier.getValue().getDatePrevisionEnvoi()), datePrevisionEnvoi);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.anyLong(), Mockito.any(Courrier.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteCourrier01OkSansDateEnvoi() {
		final Long idMariage = 10L;
		final Long idCourrier = 100L;
		final String nom = "N1";
		final String datePrevisionEnvoi = "01/01/2017";

		// ARRANGE
		final ArgumentCaptor<Courrier> argumentCaptorCourrier = ArgumentCaptor.forClass(Courrier.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idCourrier).when(this.mariageService).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorCourrier.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom,
				"datePrevisionEnvoi", datePrevisionEnvoi);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idCourrierRetour = this.getREST().postForObject(
				this.getURL() + "/mariage/" + idMariage + "/courrier", requestParam, Long.class, uriVars);

		// ASSERT
		final SimpleDateFormat sdf = (new SimpleDateFormat("dd/MM/yyyy"));
		Assert.assertNotNull(idCourrierRetour);
		Assert.assertEquals(idCourrierRetour, idCourrier);
		Assert.assertEquals(argumentCaptorCourrier.getValue().getNom(), nom);
		Assert.assertEquals(sdf.format(argumentCaptorCourrier.getValue().getDatePrevisionEnvoi()), datePrevisionEnvoi);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.anyLong(), Mockito.any(Courrier.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteCourrier02DatePrevisionInvalide() {
		final Long idMariage = 10L;
		final Long idCourrier = 100L;
		final String nom = "N1";
		final String datePrevisionEnvoi = "01-01-2017";

		// ARRANGE
		final ArgumentCaptor<Courrier> argumentCaptorCourrier = ArgumentCaptor.forClass(Courrier.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idCourrier).when(this.mariageService).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorCourrier.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("nom", nom,
				"datePrevisionEnvoi", datePrevisionEnvoi);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(this.getREST()).postForObject(
				this.getURL() + "/mariage/" + idMariage + "/courrier", requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(datePrevisionEnvoi));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test03SupprimeCourrier() {
		final Long idMariage = 10L;
		final Long idCourrier = 100L;

		// ARRANGE
		final ArgumentCaptor<Long> argumentCaptorIdICourrier = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.mariageService).supprimeCourrier(argumentCaptorIdMariage.capture(),
				argumentCaptorIdICourrier.capture());

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(
				this.getURL() + "/mariage/" + idMariage + "/courrier/" + idCourrier, HttpMethod.DELETE, null,
				Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdICourrier.getValue(), idCourrier);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).supprimeCourrier(Mockito.anyLong(), Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test04LieCourrierEtEtape() throws URISyntaxException {
		final Long idMariage = 10L;
		final Long idCourrier = 100L;
		final Long idEtape = 110L;
		final Boolean lie = true;

		// ARRANGE
		final ArgumentCaptor<Long> argumentCaptorIdCourrier = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Long> argumentCaptorIdEtape = ArgumentCaptor.forClass(Long.class);
		final ArgumentCaptor<Boolean> argumentCaptorLie = ArgumentCaptor.forClass(Boolean.class);
		Mockito.doNothing().when(this.mariageService).lieUneEtapeEtUnCourrier(argumentCaptorIdMariage.capture(),
				argumentCaptorIdEtape.capture(), argumentCaptorIdCourrier.capture(), argumentCaptorLie.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("idEtape", idEtape, "lie",
				lie);

		// ACT
		final ResponseEntity<Void> response = this.getREST().exchange(
				this.getURL() + "/mariage/" + idMariage + "/courrier/" + idCourrier, HttpMethod.POST,
				new HttpEntity<>(requestParam), Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdCourrier.getValue(), idCourrier);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Assert.assertEquals(argumentCaptorIdEtape.getValue(), idEtape);
		Assert.assertEquals(argumentCaptorLie.getValue(), lie);
		Mockito.verify(this.mariageService).lieUneEtapeEtUnCourrier(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.anyLong(), Mockito.anyBoolean());
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

}
