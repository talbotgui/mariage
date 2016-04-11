package com.github.talbotgui.mariage.metier.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
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
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.googlecode.catchexception.CatchException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecuriteServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(EtapeServiceTest.class);

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
		this.instance.creeUtilisateur(login, mdp);

		//
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from UTILISATEUR where login=?",
				new Object[] { login }, Long.class));
		Assert.assertNotEquals(mdp,
				jdbc.queryForObject("select mdp from UTILISATEUR where login=?", new Object[] { login }, String.class));
	}

	@Test
	public void test01CreeUtilisateur02KoLogin() {
		//
		final String login = "";
		final String mdp = "unBonMdp";

		//
		CatchException.catchException(this.instance).creeUtilisateur(login, mdp);

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
		CatchException.catchException(this.instance).creeUtilisateur(login, mdp);

		//
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(
				BusinessException.equals(CatchException.caughtException(), BusinessException.ERREUR_LOGIN_MDP));
	}

	@Test
	public void test02AjouteAutorisation() throws ParseException {
		//
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final String login = "monLogin";
		final String mdp = "unBonMdp";
		this.instance.creeUtilisateur(login, mdp);
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
		this.instance.creeUtilisateur(login, mdp);
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
		this.instance.creeUtilisateur(login, mdp);
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
		this.instance.creeUtilisateur(login, mdp);

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
		this.instance.creeUtilisateur(login, mdp);

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
		this.instance.creeUtilisateur(login, mdp);

		//
		CatchException.catchException(this.instance).verifieUtilisateur(mauvaisLogin, mdp);

		//
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertTrue(BusinessException.equals(CatchException.caughtException(), BusinessException.ERREUR_LOGIN));
	}

	@Test
	public void test06ListeUtilisateurs() {
		//
		this.instance.creeUtilisateur("monLogin1", "monMdp");
		this.instance.creeUtilisateur("monLogin2", "monMdp");
		this.instance.creeUtilisateur("monLogin3", "monMdp");
		this.instance.creeUtilisateur("monLogin4", "monMdp");

		//
		final Collection<Utilisateur> liste = this.instance.listeUtilisateurs();

		//
		Assert.assertEquals(4, liste.size());
	}

}
