package com.github.talbotgui.mariage.rest.controleur;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ParametreRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeEtapes() {

		// ARRANGE
		final List<String> toReturn = Arrays.asList("1", "2", "3");
		Mockito.doReturn(toReturn).when(this.mariageService).listeAgePossible();

		// ACT
		final ParameterizedTypeReference<Collection<String>> typeRetour = new ParameterizedTypeReference<Collection<String>>() {
		};
		final ResponseEntity<Collection<String>> ages = getREST().exchange(getURL() + "/parametres/age", HttpMethod.GET,
				null, typeRetour);

		// ASSERT
		Assert.assertNotNull(ages.getBody());
		Assert.assertEquals(ages.getBody(), toReturn);
		Mockito.verify(this.mariageService).listeAgePossible();
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}
}
