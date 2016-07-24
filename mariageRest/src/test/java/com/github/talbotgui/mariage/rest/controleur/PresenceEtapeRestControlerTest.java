package com.github.talbotgui.mariage.rest.controleur;

import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PresenceEtapeRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01ModifiePresenceEtape() {
		final Long idMariage = 10L;
		final Long idEtape = 20L;
		final Long idFoyer = 20L;
		final Boolean estInvite = true;

		// ARRANGE
		Mockito.doNothing().when(this.mariageService).modifieFoyerEtapeInvitation(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.anyLong(), Mockito.anyBoolean());

		// ACT
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("idMariage", idMariage,
				"idEtape", idEtape, "idFoyer", idFoyer, "estInvite", estInvite);
		final ResponseEntity<Void> response = this.getREST().exchange(
				this.getURL() + "/mariage/" + idMariage + "/foyer/" + idFoyer, HttpMethod.POST,
				new HttpEntity<>(requestParam), Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(this.mariageService).modifieFoyerEtapeInvitation(idMariage, idEtape, idFoyer, estInvite);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}
}
