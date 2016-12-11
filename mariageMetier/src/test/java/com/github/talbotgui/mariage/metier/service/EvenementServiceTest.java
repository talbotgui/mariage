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

import com.github.talbotgui.mariage.metier.entities.Evenement;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EvenementServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(EvenementServiceTest.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MariageService instance;

	@Autowired
	private SecuriteService securiteService;

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
	public void test01SauvegarderEvenement01SansParticipants() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String titre = "titre";
		final Date debut = new Date(0);
		final Date fin = new Date();

		// ACT
		this.instance.sauvegarder(idMariage, new Evenement(titre, debut, fin));

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from EVENEMENT", Long.class));
		Assert.assertEquals((Long) 0L, jdbc.queryForObject("select count(*) from EVENEMENT_PARTICIPANTS", Long.class));
	}

	@Test
	public void test01SauvegarderEvenement02AvecParticipants() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String titre = "titre";
		final Date debut = new Date(0);
		final Date fin = new Date();
		final String login = "login99";
		this.securiteService.sauvegarderUtilisateur(login, "mdp0123456", Role.UTILISATEUR);

		// ACT
		this.instance.sauvegarder(idMariage, new Evenement(titre, debut, fin, Arrays.asList(new Utilisateur(login))));

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from EVENEMENT", Long.class));
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from EVENEMENT_PARTICIPANTS", Long.class));
	}

	@Test
	public void test02ListerEvenements() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String titre = "titre";
		final Date debut = new Date(0);
		final Date fin = new Date();
		this.instance.sauvegarder(idMariage, new Evenement(titre, debut, fin));

		// ACT
		final Collection<Evenement> evenements = this.instance.listerEvenementsParIdMariage(idMariage);

		// ASSERT
		Assert.assertNotNull(evenements);
		Assert.assertEquals(1, evenements.size());
		Assert.assertEquals(titre, evenements.iterator().next().getTitre());
		Assert.assertEquals(debut, evenements.iterator().next().getDebut());
		Assert.assertEquals(fin, evenements.iterator().next().getFin());
	}

	@Test
	public void test03SupprimerEvenement() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String titre = "titre";
		final Date debut = new Date(0);
		final Date fin = new Date();
		final String login = "login99";
		this.securiteService.sauvegarderUtilisateur(login, "mdp0123456", Role.UTILISATEUR);
		final Long idEvenement = this.instance.sauvegarder(idMariage,
				new Evenement(titre, debut, fin, Arrays.asList(new Utilisateur(login))));

		// ACT
		this.instance.supprimerEvenement(idMariage, idEvenement);

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		Assert.assertEquals((Long) 0L, jdbc.queryForObject("select count(*) from EVENEMENT", Long.class));
		Assert.assertEquals((Long) 0L, jdbc.queryForObject("select count(*) from EVENEMENT_PARTICIPANTS", Long.class));
	}
}
