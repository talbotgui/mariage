package com.github.talbotgui.mariage.metier.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.googlecode.catchexception.CatchException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
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
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

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
		this.instance.sauvegardeUtilisateur(login, mdp, null);

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
		CatchException.catchException(this.instance).sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(
				BusinessException.equals(CatchException.caughtException(), BusinessException.ERREUR_LOGIN_MDP));
	}

	@Test
	public void test01CreeUtilisateur03KoMdp() {
		//
		final String login = "monLogin";
		final String mdp = "un";

		//
		CatchException.catchException(this.instance).sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(
				BusinessException.equals(CatchException.caughtException(), BusinessException.ERREUR_LOGIN_MDP));
	}

	@Test
	public void test01CreeUtilisateur04ModifOk() {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final Role role = Utilisateur.Role.UTILISATEUR;
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		this.instance.sauvegardeUtilisateur(login, mdp, role);

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
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		final Long idMariage = this.mariageService.sauvegarde(ObjectMother.creeMariageSeul());

		//
		this.instance.ajouteAutorisation(login, idMariage);

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
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		final Long idMariage = this.mariageService.sauvegarde(ObjectMother.creeMariageSeul());
		this.instance.ajouteAutorisation(login, idMariage);

		//
		this.instance.supprimeAutorisation(login, idMariage);

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
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		final Long idMariage = this.mariageService.sauvegarde(ObjectMother.creeMariageSeul());
		this.instance.ajouteAutorisation(login, idMariage);

		//
		this.instance.supprimeUtilisateur(login);

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
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		this.instance.verifieUtilisateur(login, mdp);

		//
	}

	@Test
	public void test05VerifieUtilisateur02KoMdp() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisMdp = "pasBonMdp";
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		CatchException.catchException(this.instance).verifieUtilisateur(login, mauvaisMdp);

		//
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(BusinessException.equals(CatchException.caughtException(), BusinessException.ERREUR_LOGIN));
	}

	@Test
	public void test05VerifieUtilisateur03KoLogin() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisLogin = "pasMonLogin";
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		CatchException.catchException(this.instance).verifieUtilisateur(mauvaisLogin, mdp);

		//
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(BusinessException.equals(CatchException.caughtException(), BusinessException.ERREUR_LOGIN));
	}

	@Test
	public void test06ListeUtilisateurs() {
		//
		this.instance.sauvegardeUtilisateur("monLogin1", "monMdp", Utilisateur.Role.ADMIN);
		this.instance.sauvegardeUtilisateur("monLogin2", "monMdp", Utilisateur.Role.ADMIN);
		this.instance.sauvegardeUtilisateur("monLogin3", "monMdp", Utilisateur.Role.ADMIN);
		this.instance.sauvegardeUtilisateur("monLogin4", "monMdp", Utilisateur.Role.ADMIN);

		//
		final Collection<Utilisateur> liste = this.instance.listeUtilisateurs();

		//
		Assert.assertEquals(4, liste.size());
	}

	@Test
	public void test07VerifieVerrouillageUtilisateur() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisMdp = "pasBonMdp";
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);

		//
		CatchException.catchException(this.instance).verifieUtilisateur(login, mauvaisMdp);
		final Exception e1 = CatchException.caughtException();
		CatchException.catchException(this.instance).verifieUtilisateur(login, mauvaisMdp);
		final Exception e2 = CatchException.caughtException();
		CatchException.catchException(this.instance).verifieUtilisateur(login, mauvaisMdp);
		final Exception e3 = CatchException.caughtException();

		//
		Assert.assertNotNull(e1);
		Assert.assertTrue(BusinessException.equals(e1, BusinessException.ERREUR_LOGIN));
		Assert.assertNotNull(e2);
		Assert.assertTrue(BusinessException.equals(e2, BusinessException.ERREUR_LOGIN));
		Assert.assertNotNull(e3);
		Assert.assertTrue(BusinessException.equals(e3, BusinessException.ERREUR_LOGIN_VEROUILLE));
	}

	@Test
	public void test08VerifieDeverrouillageUtilisateur() {
		//
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		final String mauvaisMdp = "pasBonMdp";
		this.instance.sauvegardeUtilisateur(login, mdp, Utilisateur.Role.ADMIN);
		CatchException.catchException(this.instance).verifieUtilisateur(login, mauvaisMdp);
		CatchException.catchException(this.instance).verifieUtilisateur(login, mauvaisMdp);
		CatchException.catchException(this.instance).verifieUtilisateur(login, mauvaisMdp);

		//
		this.instance.deverrouilleUtilisateur(login);

		//
		this.instance.verifieUtilisateur(login, mdp);
	}

	@Test
	public void test09ListeRoles() {
		//

		//
		final Collection<String> roles = this.instance.listeRolePossible();

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
		this.instance.sauvegardeUtilisateur(login, mdp, role);

		//
		final Utilisateur u = this.instance.chargeUtilisateur(login);

		//
		Assert.assertEquals(login, u.getLogin());
		Assert.assertNotEquals(mdp, u.getMdp());
		Assert.assertEquals(role, u.getRole());
	}
}
