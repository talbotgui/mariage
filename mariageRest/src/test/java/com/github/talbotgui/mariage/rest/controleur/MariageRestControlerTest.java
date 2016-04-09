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
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
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

		// Arrange
		final Long idMariage = 10L;
		final Collection<Mariage> toReturn = Arrays.asList(new Mariage(idMariage, new Date(), "Mme.", "M."));
		Mockito.doReturn(toReturn).when(super.service).listeTousMariages();

		// ACT
		final ParameterizedTypeReference<Collection<MariageDTO>> typeRetour = new ParameterizedTypeReference<Collection<MariageDTO>>() {
		};
		final ResponseEntity<Collection<MariageDTO>> mariages = getREST().exchange(getURL() + "/mariage",
				HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(mariages.getBody().size(), 1);
		Assert.assertEquals(mariages.getBody().iterator().next().getId(), idMariage);
		Mockito.verify(this.service).listeTousMariages();
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02GetMariageParId() {

		// ARRANGE
		final Long idMariage = 10L;
		final Mariage toReturn = new Mariage(idMariage, new Date(), "Mme.", "M.");
		Mockito.doReturn(toReturn).when(this.service).chargeMariageParId(Mockito.anyLong());

		// ACT
		final ResponseEntity<MariageDTO> mariage = getREST().exchange(getURL() + "/mariage/" + idMariage,
				HttpMethod.GET, null, MariageDTO.class);

		// ASSERT
		Assert.assertEquals(mariage.getBody().getId(), idMariage);
		Mockito.verify(this.service).chargeMariageParId(idMariage);
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test03SauvegardeMariageOk() {

		// ARRANGE
		final Long idMariage = 10L;
		final ArgumentCaptor<Mariage> argumentCaptor = ArgumentCaptor.forClass(Mariage.class);
		Mockito.doReturn(idMariage).when(this.service).sauvegarde(argumentCaptor.capture());

		final String dateCelebration = "01/01/2017";
		final String marie1 = "M";
		final String marie2 = "G";
		final MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("dateCelebration", dateCelebration);
		requestParam.add("marie1", marie1);
		requestParam.add("marie2", marie2);
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
		Mockito.verify(this.service).sauvegarde(Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(this.service);

	}

	@Test
	public void test04SauvegardeMariageKoFormatDate() {

		// ARRANGE
		final String dateCelebration = "01-01-2017";
		final String marie1 = "M";
		final String marie2 = "G";
		final MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("dateCelebration", dateCelebration);
		requestParam.add("marie1", marie1);
		requestParam.add("marie2", marie2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(getURL() + "/mariage", requestParam, Long.class,
				uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(CatchException.caughtException() instanceof HttpStatusCodeException);
		Assert.assertTrue(((HttpStatusCodeException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(dateCelebration));
		Mockito.verifyNoMoreInteractions(this.service);
	}

}
