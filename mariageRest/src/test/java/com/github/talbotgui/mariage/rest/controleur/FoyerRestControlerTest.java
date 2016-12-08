package com.github.talbotgui.mariage.rest.controleur;

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
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.rest.controleur.dto.ReponseAvecChoix;

public class FoyerRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetListeFoyers() {
		final Long idMariage = 10L;
		final List<Foyer> foyersToReturn = Arrays.asList(new Foyer(1L, "G1", "F1", "ADD1", "EMAIL1", "01"),
				new Foyer(2L, "G1", "F2", "ADD2", "EMAIL2", "02"), new Foyer(3L, "G2", "F3", "ADD3", "EMAIL3", "03"));
		final List<Courrier> courrierToReturn = Arrays.asList(new Courrier(1L, "C1", new Date()),
				new Courrier(2L, "C2", new Date()), new Courrier(3L, "C4", new Date()),
				new Courrier(4L, "C3", new Date()), new Courrier(5L, "C5", new Date()));

		// ARRANGE
		Mockito.doReturn(courrierToReturn).when(this.mariageService).listerCourriersParIdMariage(Mockito.anyLong());
		Mockito.doReturn(foyersToReturn).when(this.mariageService).listerFoyersParIdMariage(Mockito.anyLong());

		// ACT
		final ParameterizedTypeReference<ReponseAvecChoix> typeRetour = new ParameterizedTypeReference<ReponseAvecChoix>() {
		};
		final ResponseEntity<ReponseAvecChoix> foyers = this.getREST()
				.exchange(this.getURL() + "/mariage/" + idMariage + "/foyer", HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(foyers.getBody().getChoixPossibles().size(), 5);
		Assert.assertEquals(foyers.getBody().getDtos().size(), 3);
		Mockito.verify(this.mariageService).listerCourriersParIdMariage(idMariage);
		Mockito.verify(this.mariageService).listerFoyersParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteFoyer01FoyerInexistant() {
		final Long idMariage = 10L;
		final Long idFoyer = 100L;
		final String nom = "F1";
		final String groupe = "G1";

		// ARRANGE
		final ArgumentCaptor<Foyer> argumentCaptorFoyer = ArgumentCaptor.forClass(Foyer.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idFoyer).when(this.mariageService).sauvegarder(argumentCaptorIdMariage.capture(),
				argumentCaptorFoyer.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("foyer", nom, "groupe",
				groupe);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idFoyerRetour = this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/foyer",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idFoyerRetour);
		Assert.assertEquals(idFoyerRetour, idFoyer);
		Assert.assertEquals(argumentCaptorFoyer.getValue().getNom(), nom);
		Assert.assertEquals(argumentCaptorFoyer.getValue().getGroupe(), groupe);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarder(Mockito.anyLong(), Mockito.any(Foyer.class));
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test02AjouteFoyer02FoyerExistant() {
		final Long idMariage = 10L;
		final Long idFoyer = 100L;
		final String nom1 = "F1";
		final String nom2 = "F1";
		final String groupe1 = "G1";
		final String groupe2 = "G1";
		final String adresse1 = "add1";
		final String email1 = "email1";
		final String telephone1 = "telephone1";
		final String adresse2 = "add2";
		final String email2 = "email2";
		final String telephone2 = "telephone2";

		// ARRANGE
		final ArgumentCaptor<Foyer> argumentCaptorFoyer = ArgumentCaptor.forClass(Foyer.class);
		final ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(new Foyer(idFoyer, groupe1, nom1, adresse1, email1, telephone1)).when(this.mariageService)
				.chargerFoyerParId(idFoyer);
		Mockito.doReturn(idFoyer).when(this.mariageService).sauvegarder(argumentCaptorIdMariage.capture(),
				argumentCaptorFoyer.capture());

		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("foyer", nom2, "groupe",
				groupe2, "id", idFoyer, "adresse", adresse2, "telephone", telephone2, "email", email2);
		final Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		final Long idFoyerRetour = this.getREST().postForObject(this.getURL() + "/mariage/" + idMariage + "/foyer",
				requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idFoyerRetour);
		Assert.assertEquals(idFoyerRetour, idFoyer);
		Assert.assertEquals(argumentCaptorFoyer.getValue().getNom(), nom2);
		Assert.assertEquals(argumentCaptorFoyer.getValue().getGroupe(), groupe2);
		Assert.assertEquals(argumentCaptorFoyer.getValue().getAdresse(), adresse2);
		Assert.assertEquals(argumentCaptorFoyer.getValue().getEmail(), email2);
		Assert.assertEquals(argumentCaptorFoyer.getValue().getTelephone(), telephone2);
		Assert.assertEquals(argumentCaptorIdMariage.getValue(), idMariage);
		Mockito.verify(this.mariageService).sauvegarder(Mockito.anyLong(), Mockito.any(Foyer.class));
		Mockito.verify(this.mariageService).chargerFoyerParId(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}

	@Test
	public void test03LieUnFoyerEtUnCourrier() {
		final Long idMariage = 10L;
		final Long idCourrier = 20L;
		final Long idFoyer = 20L;
		final Boolean estInvite = true;

		// ARRANGE
		Mockito.doNothing().when(this.mariageService).lierUnFoyerEtUnCourrier(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.anyLong(), Mockito.anyBoolean());

		// ACT
		final MultiValueMap<String, Object> requestParam = ControlerTestUtil.creeMapParamRest("idMariage", idMariage,
				"idCourrier", idCourrier, "idFoyer", idFoyer, "estInvite", estInvite);
		final ResponseEntity<Void> response = this.getREST().exchange(
				this.getURL() + "/mariage/" + idMariage + "/foyer/" + idFoyer, HttpMethod.POST,
				new HttpEntity<>(requestParam), Void.class);

		// ASSERT
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(this.mariageService).lierUnFoyerEtUnCourrier(idMariage, idCourrier, idFoyer, estInvite);
		Mockito.verifyNoMoreInteractions(this.mariageService);
	}
}
