package com.github.talbotgui.mariage.rest.controleur;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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

import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.rest.controleur.dto.MariageDTO;
import com.googlecode.catchexception.CatchException;

public class MariageRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeMariages() {
		final Long idMariage = 10L;
		final Collection<Mariage> toReturn = Arrays.asList(new Mariage(idMariage, new Date(), "Mme.", "M."));

		// Arrange
		Mockito.doReturn(toReturn).when(super.mariageService).listeTousMariages();

		// ACT
		final ParameterizedTypeReference<Collection<MariageDTO>> typeRetour = new ParameterizedTypeReference<Collection<MariageDTO>>() {
		};
		final ResponseEntity<Collection<MariageDTO>> mariages = getREST().exchange(getURL() + "/mariage",
				HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(mariages.getBody().size(), 1);
		Assert.assertEquals(mariages.getBody().iterator().next().getId(), idMariage);
		Mockito.verify(this.mariageService).listeTousMariages();
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02GetMariageParId() {
		final Long idMariage = 10L;
		final Mariage toReturn = new Mariage(idMariage, new Date(), "Mme.", "M.");

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.mariageService).chargeMariageParId(Mockito.anyLong());

		// ACT
		final ResponseEntity<MariageDTO> mariage = getREST().exchange(getURL() + "/mariage/" + idMariage,
				HttpMethod.GET, null, MariageDTO.class);

		// ASSERT
		Assert.assertEquals(mariage.getBody().getId(), idMariage);
		Mockito.verify(this.mariageService).chargeMariageParId(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test03SauvegardeMariageOk() {
		final Long idMariage = 10L;
		final String dateCelebration = "01/01/2017";
		final String marie1 = "M";
		final String marie2 = "G";

		// ARRANGE
		final ArgumentCaptor<Mariage> argumentCaptor = ArgumentCaptor.forClass(Mariage.class);
		Mockito.doReturn(idMariage).when(this.mariageService).sauvegarde(argumentCaptor.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("dateCelebration",
				dateCelebration, "marie1", marie1, "marie2", marie2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idMariageRetour = getREST().postForObject(getURL() + "/mariage", requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idMariageRetour);
		Assert.assertEquals(idMariage, idMariageRetour);
		Assert.assertEquals((new SimpleDateFormat("dd/MM/yyyy")).format(argumentCaptor.getValue().getDateCelebration()),
				dateCelebration);
		Assert.assertEquals(argumentCaptor.getValue().getMarie1(), marie1);
		Assert.assertEquals(argumentCaptor.getValue().getMarie2(), marie2);
		Mockito.verify(this.mariageService).sauvegarde(Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(this.mariageService);

	}

	@Test
	public void test04SauvegardeMariageKoFormatDate() {
		final String dateCelebration = "01-01-2017";
		final String marie1 = "M";
		final String marie2 = "G";

		// ARRANGE
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("dateCelebration",
				dateCelebration, "marie1", marie1, "marie2", marie2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage", requestParam, Long.class,
				uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(dateCelebration));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test05SupprimeMariage() {

		final Long idMariage = 10L;

		// ARRANGE
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(this.mariageService).suprimeMariage(argumentCaptorIdMariage.capture());

		// ACT
		final ResponseEntity<Void> response = getREST().exchange(getURL() + "/mariage/" + idMariage, HttpMethod.DELETE,
				null, Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).suprimeMariage(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

}
