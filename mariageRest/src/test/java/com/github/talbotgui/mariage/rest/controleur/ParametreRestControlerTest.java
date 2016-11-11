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

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public class ParametreRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeAge() {
		final List<String> toReturn = Arrays.asList("1", "2", "3");

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.mariageService).listeAgePossible();

		// ACT
		final ParameterizedTypeReference<Collection<String>> typeRetour = new ParameterizedTypeReference<Collection<String>>() {
		};
		final ResponseEntity<Collection<String>> ages = this.getREST().exchange(this.getURL() + "/parametres/age",
				HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertNotNull(ages.getBody());
		Assert.assertEquals(ages.getBody(), toReturn);
		Mockito.verify(this.mariageService).listeAgePossible();
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02GetListeRole() {
		final List<String> toReturn = Arrays.asList(Utilisateur.Role.ADMIN.toString(),
				Utilisateur.Role.UTILISATEUR.toString(), Utilisateur.Role.MARIE.toString());

		// ARRANGE
		Mockito.doReturn(toReturn).when(this.securiteService).listeRolePossible();

		// ACT
		final ParameterizedTypeReference<Collection<String>> typeRetour = new ParameterizedTypeReference<Collection<String>>() {
		};
		final ResponseEntity<Collection<String>> ages = this.getREST().exchange(this.getURL() + "/parametres/role",
				HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertNotNull(ages.getBody());
		Assert.assertEquals(ages.getBody(), toReturn);
		Mockito.verify(this.securiteService).listeRolePossible();
		Mockito.verifyNoMoreInteractions(this.securiteService);
	}
}
