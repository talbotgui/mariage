package com.github.talbotgui.mariage.rest.controleur;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.application.RestApplication;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.MariageDTO;
import com.googlecode.catchexception.CatchException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MariageRestControlerTest {

	private static final Logger LOG = LoggerFactory.getLogger(MariageRestControlerTest.class);

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

	private static final String URL = "http://localhost:8080";

	@AfterClass
	public static void afterClass() {
		RestApplication.stop();
	}

	@BeforeClass
	public static void beforeClass() throws ParseException {

		// Start application
		RestApplication.start();

		// Create data
		MariageService service = RestApplication.getApplicationContext().getBean(MariageService.class);
		service.sauvegardeGrappe(ObjectMother.creeMariageSimple());
	}

	private RestTemplate rest;

	@Before
	public void before() {
		// Reset restTemplate
		this.rest = new RestTemplate();
		rest.setInterceptors(REST_INTERCEPTORS);
	}

	@Test
	public void test01GetListeMariages() {

		// ACT
		ParameterizedTypeReference<Collection<MariageDTO>> typeRetour = new ParameterizedTypeReference<Collection<MariageDTO>>() {
		};
		ResponseEntity<Collection<MariageDTO>> mariages = rest.exchange(URL + "/mariage", HttpMethod.GET, null,
				typeRetour);

		// ASSERT
		Assert.assertEquals(1, mariages.getBody().size());
	}

	@Test
	public void test02GetMariageParId() {

		// ARRANGE

		// ACT
		ResponseEntity<MariageDTO> mariage = rest.exchange(URL + "/mariage/0", HttpMethod.GET, null, MariageDTO.class);

		// ASSERT
		Assert.assertEquals(Long.valueOf(0), mariage.getBody().getId());
	}

	@Test
	public void test03GetListeInvites() {

		// ARRANGE

		// ACT
		ParameterizedTypeReference<Collection<InviteDTO>> typeRetour = new ParameterizedTypeReference<Collection<InviteDTO>>() {
		};
		ResponseEntity<Collection<InviteDTO>> invites = rest.exchange(URL + "/mariage/0/invites", HttpMethod.GET, null,
				typeRetour);

		// ASSERT
		Assert.assertEquals(10, invites.getBody().size());
	}

	@Test
	public void test04CreeMariageOk() {

		// ARRANGE
		final String dateCelebration = "15/07/2016 13:00";
		final String marie1 = "Marie";
		final String marie2 = "Guillaume";
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("dateCelebration", dateCelebration);
		requestParam.add("marie1", marie1);
		requestParam.add("marie2", marie2);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		Long idMariage = rest.postForObject(URL + "/mariage", requestParam, Long.class, uriVars);
		ResponseEntity<MariageDTO> mariage = rest.exchange(URL + "/mariage/" + idMariage, HttpMethod.GET, null,
				MariageDTO.class);

		// ASSERT
		Assert.assertNotNull(idMariage);
		Assert.assertEquals(idMariage, mariage.getBody().getId());
		Assert.assertEquals(dateCelebration, mariage.getBody().getDateCelebration());
		Assert.assertEquals(marie1, mariage.getBody().getMarie1());
		Assert.assertEquals(marie2, mariage.getBody().getMarie2());
	}

	@Test
	public void test05CreeMariageKoFormatDate() {

		// ARRANGE
		final String dateCelebration = "15-07-2016 13:00";
		final String marie1 = "Marie";
		final String marie2 = "Guillaume";
		MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		requestParam.add("dateCelebration", dateCelebration);
		requestParam.add("marie1", marie1);
		requestParam.add("marie2", marie2);
		Map<String, Object> uriVars = new HashMap<String, Object>();

		// ACT
		CatchException.catchException(rest).postForObject(URL + "/mariage", requestParam, Long.class, uriVars);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(((HttpClientErrorException) CatchException.caughtException()).getResponseBodyAsString()
				.contains(dateCelebration));
	}

}
