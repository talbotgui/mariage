package com.github.talbotgui.mariage.rest.controleur;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.metier.service.SecuriteService;
import com.github.talbotgui.mariage.rest.application.RestTestApplication;

@SpringApplicationConfiguration(classes = { RestTestApplication.class })
@WebIntegrationTest(randomPort = true)
public class BaseRestControlerTest extends AbstractTestNGSpringContextTests {

	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(BaseRestControlerTest.class);

	/** Instance des controleurs nécessaires pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private CourrierRestControler courrierCtrl;

	/** Instance des controleurs nécessaires pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private EtapeRestControler etapeCtrl;

	/** Instance des controleurs nécessaires pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private InviteRestControler inviteCtrl;

	/** Instance des controleurs nécessaires pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private MariageRestControler mariageCtrl;

	/** Mock de service créé par Mockito. */
	@Mock
	protected MariageService mariageService;

	/** Instance des controleurs nécessaires pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private ParametreRestControler parametreCtrl;

	/** Instance des controleurs nécessaires pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private PresenceEtapeRestControler peCtrl;

	@Value("${local.server.port}")
	private int port;

	/** Mock de service créé par Mockito. */
	@Mock
	protected SecuriteService securiteService;

	/** Instance des controleurs nécessaires pour y injecter le mock de service. */
	@Autowired
	@InjectMocks
	private UtilisateurRestControler utilisateurCtrl;

	/** Pour créer les mock. */
	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
	}

	/** Pour faire un reset de chaque mock. */
	@BeforeMethod
	public void beforeMethod() {
		LOG.info("*****************************************");
		Mockito.reset(this.mariageService, this.securiteService);
	}

	/**
	 * Reset restTemplate avec les intercepteurs
	 *
	 * @see ControlerTestUtil.REST_INTERCEPTORS
	 *
	 * @return a new RestTemplate
	 */
	protected RestTemplate getREST() {
		final RestTemplate rest = new RestTemplate();
		rest.setInterceptors(ControlerTestUtil.REST_INTERCEPTORS);
		return rest;
	}

	/** Test URL. */
	protected String getURL() {
		return "http://localhost:" + this.port;
	}

}
