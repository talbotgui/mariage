package com.github.talbotgui.mariage.metier.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.talbotgui.mariage.metier.entities.Evenement;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.github.talbotgui.mariage.metier.exception.BusinessException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecuriteServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(SecuriteServiceTest.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SecuriteService instance;

	@Autowired
	private MariageService mariageService;

	@Before
	public void before() throws IOException, URISyntaxException {
		LOG.info("---------------------------------------------------------");

		// Destruction des données
		final Collection<String> strings = Files
				.readAllLines(Paths.get(ClassLoader.getSystemResource("sql/dataPurge.sql").toURI()));
		final String[] requetes = strings.toArray(new String[strings.size()]);
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		LOG.info("Execute SQL : {}", (Object[]) requetes);
		jdbc.batchUpdate(requetes);

	}

	@Test
	public void test01CreeUtilisateur01Ok() {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";

		//
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from UTILISATEUR where login=?",
				new Object[] { login }, Long.class));
		Assert.assertNotEquals(mdp,
				jdbc.queryForObject("select mdp from UTILISATEUR where login=?", new Object[] { login }, String.class));
	}

	@Test
	public void test01CreeUtilisateur01OkSansRole() {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";

		//
		this.instance.sauvegarderUtilisateur(login, mdp, null);

		//
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from UTILISATEUR where login=?",
				new Object[] { login }, Long.class));
		Assert.assertNotEquals(mdp,
				jdbc.queryForObject("select mdp from UTILISATEUR where login=?", new Object[] { login }, String.class));
		Assert.assertEquals(Role.UTILISATEUR.name(), jdbc.queryForObject("select role from UTILISATEUR where login=?",
				new Object[] { login }, String.class));
	}

	@Test
	public void test01CreeUtilisateur02KoLogin() {
		//
		final String login = "";
		final String mdp = "unBonMdp";

		//
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		});

		//
		Assert.assertNotNull(thrown);
		Assert.assertTrue(BusinessException.equals((Exception) thrown, BusinessException.ERREUR_LOGIN_MDP));
	}

	@Test
	public void test01CreeUtilisateur03KoMdp() {
		//
		final String login = "monLogin";
		final String mdp = "un";

		//
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		});

		//
		Assert.assertNotNull(thrown);
		Assert.assertTrue(BusinessException.equals((Exception) thrown, BusinessException.ERREUR_LOGIN_MDP));
	}

	@Test
	public void test01CreeUtilisateur04ModifOk() {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final Role role = Utilisateur.Role.UTILISATEUR;
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		this.instance.sauvegarderUtilisateur(login, mdp, role);

		//
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from UTILISATEUR where login=?",
				new Object[] { login }, Long.class));
		Assert.assertNotEquals(mdp,
				jdbc.queryForObject("select mdp from UTILISATEUR where login=?", new Object[] { login }, String.class));
		Assert.assertEquals(role.toString(), jdbc.queryForObject("select role from UTILISATEUR where login=?",
				new Object[] { login }, String.class));
	}

	@Test
	public void test02AjouteAutorisation() throws ParseException {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		final Long idMariage = this.mariageService.sauvegarder(ObjectMother.creeMariageSeul());

		//
		this.instance.ajouterAutorisation(login, idMariage);

		//
		Assert.assertEquals((Long) 1L,
				jdbc.queryForObject("select count(*) from AUTORISATION where ID_MARIAGE=? and ID_UTILISATEUR=?",
						new Object[] { idMariage, login }, Long.class));
	}

	@Test
	public void test03SupprimeAutorisation() throws ParseException {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		final Long idMariage = this.mariageService.sauvegarder(ObjectMother.creeMariageSeul());
		final Long id = this.instance.ajouterAutorisation(login, idMariage);

		//
		this.instance.supprimerAutorisation(id);

		//
		Assert.assertEquals((Long) 0L,
				jdbc.queryForObject("select count(*) from AUTORISATION where ID_MARIAGE=? and ID_UTILISATEUR=?",
						new Object[] { idMariage, login }, Long.class));
	}

	@Test
	public void test04SupprimeUtilisateur() throws ParseException {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		final Long idMariage = this.mariageService.sauvegarder(ObjectMother.creeMariageSeul());
		this.instance.ajouterAutorisation(login, idMariage);
		this.mariageService.sauvegarder(idMariage,
				new Evenement("EV1", new Date(), new Date(), Arrays.asList(new Utilisateur(login))));

		//
		this.instance.supprimerUtilisateur(login);

		//
		Assert.assertEquals((Long) 0L,
				jdbc.queryForObject("select count(*) from AUTORISATION where ID_MARIAGE=? and ID_UTILISATEUR=?",
						new Object[] { idMariage, login }, Long.class));
		Assert.assertEquals((Long) 0L, jdbc.queryForObject("select count(*) from UTILISATEUR where login=?",
				new Object[] { login }, Long.class));
		Assert.assertEquals((Long) 1L,
				jdbc.queryForObject("select count(*) from MARIAGE where id=?", new Object[] { idMariage }, Long.class));
	}

	@Test
	public void test05VerifieUtilisateur01Ok() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		this.instance.verifierUtilisateur(login, mdp);

		//
	}

	@Test
	public void test05VerifieUtilisateur02KoMdp() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisMdp = "pasBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});

		//
		Assert.assertNotNull(thrown);
		Assert.assertTrue(BusinessException.equals((Exception) thrown, BusinessException.ERREUR_LOGIN));
	}

	@Test
	public void test05VerifieUtilisateur03KoLogin() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisLogin = "pasMonLogin";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(mauvaisLogin, mdp);
		});

		//
		Assert.assertNotNull(thrown);
		Assert.assertTrue(BusinessException.equals((Exception) thrown, BusinessException.ERREUR_LOGIN));
	}

	@Test
	public void test06ListeUtilisateurs01() {
		//
		this.instance.sauvegarderUtilisateur("monLogin1", "monMdp", Utilisateur.Role.ADMIN);
		this.instance.sauvegarderUtilisateur("monLogin2", "monMdp", Utilisateur.Role.ADMIN);
		this.instance.sauvegarderUtilisateur("monLogin3", "monMdp", Utilisateur.Role.ADMIN);
		this.instance.sauvegarderUtilisateur("monLogin4", "monMdp", Utilisateur.Role.ADMIN);

		//
		final Collection<Utilisateur> liste = this.instance.listerUtilisateurs();

		//
		Assert.assertEquals(4, liste.size());
	}

	@Test
	public void test06ListeUtilisateurs02ParMariage() {
		//
		final Collection<String> logins = Arrays.asList("monLogin1", "monLogin2", "monLogin3", "monLogin4");
		final Long idMariage = this.mariageService.sauvegarder(new Mariage(new Date(), "Marie1", "Marie2"));
		for (final String login : logins) {
			this.instance.sauvegarderUtilisateur(login, "monMdp", Utilisateur.Role.ADMIN);
			this.instance.ajouterAutorisation(login, idMariage);
		}

		//
		final Collection<Utilisateur> liste = this.instance.listerUtilisateursParMariage(idMariage);

		//
		Assert.assertEquals(logins.size(), liste.size());
	}

	@Test
	public void test07VerifieVerrouillageUtilisateur() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisMdp = "pasBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		final Throwable e1 = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});
		final Throwable e2 = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});
		final Throwable e3 = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});
		final Throwable e4 = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mdp);
		});
		final Throwable e5 = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});
		final Throwable e6 = Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});

		//
		Assert.assertNotNull(e1);
		Assert.assertTrue(BusinessException.equals((Exception) e1, BusinessException.ERREUR_LOGIN));
		Assert.assertNotNull(e2);
		Assert.assertTrue(BusinessException.equals((Exception) e2, BusinessException.ERREUR_LOGIN));
		Assert.assertNotNull(e3);
		Assert.assertTrue(BusinessException.equals((Exception) e3, BusinessException.ERREUR_LOGIN));
		Assert.assertNotNull(e4);
		Assert.assertTrue(BusinessException.equals((Exception) e4, BusinessException.ERREUR_LOGIN_VEROUILLE));
		Assert.assertNotNull(e5);
		Assert.assertTrue(BusinessException.equals((Exception) e5, BusinessException.ERREUR_LOGIN_VEROUILLE));
		Assert.assertNotNull(e6);
		Assert.assertTrue(BusinessException.equals((Exception) e6, BusinessException.ERREUR_LOGIN_VEROUILLE));
	}

	@Test
	public void test08VerifieDeverrouillageUtilisateur() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisMdp = "pasBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});
		Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});
		Assertions.catchThrowable(() -> {
			this.instance.verifierUtilisateur(login, mauvaisMdp);
		});

		//
		this.instance.deverrouillerUtilisateur(login);

		//
		this.instance.verifierUtilisateur(login, mdp);
	}

	@Test
	public void test09ListeRoles() {
		//

		//
		final Collection<String> roles = this.instance.listerRolePossible();

		//
		Assert.assertNotNull(roles);
		Assert.assertEquals(Arrays.asList(Role.values()).toString(), roles.toString());

	}

	@Test
	public void test10ChargeUtilisateur() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final Role role = Utilisateur.Role.ADMIN;
		this.instance.sauvegarderUtilisateur(login, mdp, role);

		//
		final Utilisateur u = this.instance.chargerUtilisateur(login);

		//
		Assert.assertEquals(login, u.getLogin());
		Assert.assertNotEquals(mdp, u.getMdp());
		Assert.assertEquals(role, u.getRole());
	}

	@Test
	public void test11ListeAutorisations() {
		//
		final Collection<String> logins = Arrays.asList("monLogin1", "monLogin2", "monLogin3", "monLogin4");
		final Long idMariage = this.mariageService.sauvegarder(new Mariage(new Date(), "Marie1", "Marie2"));
		for (final String login : logins) {
			this.instance.sauvegarderUtilisateur(login, "monMdp", Utilisateur.Role.ADMIN);
			this.instance.ajouterAutorisation(login, idMariage);
		}

		//
		final Collection<Autorisation> liste = this.instance.listerAutorisations();

		//
		Assert.assertEquals(4, liste.size());
	}

	@Test
	public void test12ResetPassword() {
		//
		final String login = "monLoginToReset";
		final String mdp = "unBonMdp";
		this.instance.sauvegarderUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		this.instance.verifierUtilisateur(login, mdp);

		//
		this.instance.resetPassword(login);

		//
		this.instance.verifierUtilisateur(login, login);
	}

}
