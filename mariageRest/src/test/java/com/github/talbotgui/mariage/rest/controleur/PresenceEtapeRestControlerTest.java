package com.github.talbotgui.mariage.rest.controleur;

import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PresenceEtapeRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01ModifiePresenceEtape() {

		// ARRANGE
		final Long idMariage = 10L;
		final Long id = 20L;
		final Boolean presence = true;
		Mockito.doNothing().when(this.mariageService).modifiePresenceEtape(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.anyBoolean());

		// ACT
		final MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("idMariage", idMariage);
		requestParam.add("id", id);
		requestParam.add("presence", presence);
		final ResponseEntity<Void> response = getREST().exchange(getURL() + "/mariage/" + idMariage + "/presenceEtape",
				HttpMethod.POST, new HttpEntity<>(requestParam), Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(this.mariageService).modifiePresenceEtape(idMariage, id, presence);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}
}
