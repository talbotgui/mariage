package com.github.talbotgui.mariage.rest.controleur;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.application.RestTestApplication;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.MariageDTO;
import com.googlecode.catchexception.CatchException;

@SpringApplicationConfiguration(classes = { RestTestApplication.class })
@WebIntegrationTest
public class MariageRestControlerTest extends AbstractTestNGSpringContextTests {

	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MariageRestControlerTest.class);

	/** Log interceptor for all HTTP requests. */
	private static final List<ClientHttpRequestInterceptor> REST_INTERCEPTORS = Arrays
			.asList(new ClientHttpRequestInterceptor() {
				public ClientHttpResponse intercept(HttpRequest request, byte[] body,
						ClientHttpRequestExecution execution) throws IOException {
					LOG.info("Request : {URI={}, method={}, headers={}, body={}}", request.getURI(),
							request.getMethod().name(), request.getHeaders(), new String(body));
					ClientHttpResponse response = execution.execute(request, body);
					LOG.info("Response : {code={}}", response.getStatusCode());
					return response;
				}
			});

	/** Test URL. */
	private static final String URL = "http://localhost:9000";

	/** Instance du controleur nécessaire pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private MariageRestControler ctrl;

	/** Mock de service créé par Mockito. */
	@Mock
	private MariageService service;

	/** Pour créer les mock. */
	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
	}

	/** Pour faire un reset de chaque mock. */
	@BeforeMethod
	public void beforeMethod() {
		LOG.info("*****************************************");
		Mockito.reset(this.service);
	}

	/**
	 * Reset restTemplate
	 *
	 * @return
	 */
	private RestTemplate getREST() {
		RestTemplate rest = new RestTemplate();
		rest.setInterceptors(REST_INTERCEPTORS);
		return rest;
	}

	@Test
	public void test01GetListeMariages() {

		// Arrange
		Long idMariage = 10L;
		Collection<Mariage> toReturn = Arrays.asList(new Mariage(idMariage, new Date(), "Mme.", "M."));
		Mockito.doReturn(toReturn).when(this.service).listeTousMariages();

		// ACT
		ParameterizedTypeReference<Collection<MariageDTO>> typeRetour = new ParameterizedTypeReference<Collection<MariageDTO>>() {
		};
		ResponseEntity<Collection<MariageDTO>> mariages = getREST().exchange(URL + "/mariage", HttpMethod.GET, null,
				typeRetour);

		// ASSERT
		Assert.assertEquals(1, mariages.getBody().size());
		Assert.assertEquals(idMariage, mariages.getBody().iterator().next().getId());
		Mockito.verify(this.service).listeTousMariages();
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test02GetMariageParId() {

		// ARRANGE
		Long idMariage = 10L;
		Mariage toReturn = new Mariage(idMariage, new Date(), "Mme.", "M.");
		Mockito.doReturn(toReturn).when(this.service).chargeMariageParId(Mockito.anyLong());

		// ACT
		ResponseEntity<MariageDTO> mariage = getREST().exchange(URL + "/mariage/" + idMariage, HttpMethod.GET, null,
				MariageDTO.class);

		// ASSERT
		Assert.assertEquals(idMariage, mariage.getBody().getId());
		Mockito.verify(this.service).chargeMariageParId(idMariage);
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test03GetListeInvites() {

		// ARRANGE
		Long idMariage = 10L;
		List<Invite> toReturn = Arrays.asList(new Invite("G1", "I1"), new Invite("G1", "I2"), new Invite("G1", "I3"),
				new Invite("G1", "I4"), new Invite("G2", "I1"), new Invite("G2", "I2"), new Invite("G2", "I3"),
				new Invite("G2", "I4"));
		Mockito.doReturn(toReturn).when(this.service).listeInvitesParIdMariage(Mockito.anyLong());

		// ACT
		ParameterizedTypeReference<Collection<InviteDTO>> typeRetour = new ParameterizedTypeReference<Collection<InviteDTO>>() {
		};
		ResponseEntity<Collection<InviteDTO>> invites = getREST().exchange(URL + "/mariage/" + idMariage + "/invite",
				HttpMethod.GET, null, typeRetour);

		// ASSERT
		Assert.assertEquals(8, invites.getBody().size());
		Mockito.verify(this.service).listeInvitesParIdMariage(idMariage);
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test04SauvegardeMariageOk() {

		// ARRANGE
		Long idMariage = 10L;
		ArgumentCaptor<Mariage> argumentCaptor = ArgumentCaptor.forClass(Mariage.class);
		Mockito.doReturn(idMariage).when(this.service).sauvegarde(argumentCaptor.capture());

		final String dateCelebration = "15/07/2016";
		final String marie1 = "marie1";
		final String marie2 = "marie2";
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("dateCelebration", dateCelebration);
		requestParam.add("marie1", marie1);
		requestParam.add("marie2", marie2);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Long idMariageRetour = getREST().postForObject(URL + "/mariage", requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idMariageRetour);
		Assert.assertEquals(idMariageRetour, idMariage);
		Assert.assertEquals(dateCelebration,
				(new SimpleDateFormat("dd/MM/yyyy")).format(argumentCaptor.getValue().getDateCelebration()));
		Assert.assertEquals(marie1, argumentCaptor.getValue().getMarie1());
		Assert.assertEquals(marie2, argumentCaptor.getValue().getMarie2());
		Mockito.verify(this.service).sauvegarde(Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(this.service);

	}

	@Test
	public void test05SauvegardeMariageKoFormatDate() {

		// ARRANGE
		final String dateCelebration = "15-07-2016";
		final String marie1 = "Marie";
		final String marie2 = "Guillaume";
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("dateCelebration", dateCelebration);
		requestParam.add("marie1", marie1);
		requestParam.add("marie2", marie2);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(getREST()).postForObject(URL + "/mariage", requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(((HttpClientErrorException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(dateCelebration));
		Mockito.verifyNoMoreInteractions(this.service);
	}

	@Test
	public void test06AjouteInvite() {

		// ARRANGE
		Long idMariage = 10L;
		Long idInvite = 100L;
		ArgumentCaptor<Invite> argumentCaptorInvite = ArgumentCaptor.forClass(Invite.class);
		ArgumentCaptor<Long> argumentCaptorIdMariage = ArgumentCaptor.forClass(Long.class);
		Mockito.doReturn(idInvite).when(this.service).sauvegarde(argumentCaptorIdMariage.capture(),
				argumentCaptorInvite.capture());

		final String nom = "InviteA";
		final String groupe = "Groupe1";
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("nom", nom);
		requestParam.add("groupe", groupe);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Long idInviteRetour = getREST().postForObject(URL + "/mariage/" + idMariage + "/invite", requestParam,
				Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(idInviteRetour);
		Assert.assertEquals(idInvite, idInviteRetour);
		Assert.assertEquals(nom, argumentCaptorInvite.getValue().getNom());
		Assert.assertEquals(groupe, argumentCaptorInvite.getValue().getGroupe());
		Assert.assertEquals(idMariage, argumentCaptorIdMariage.getValue());
		Mockito.verify(this.service).sauvegarde(Mockito.anyLong(), Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(this.service);

	}

}
