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

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.Presence;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MariageServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(MariageServiceTest.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MariageService instance;

	@Autowired
	private SecuriteService securiteInstance;

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
	public void test01CreationMariage() throws ParseException {

		// ARRANGE
		final Mariage mariage = ObjectMother.creeMariageSimple();

		// ACT
		this.instance.sauvegarderGrappe(mariage);

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		Assert.assertEquals((Long) 1L, jdbc.queryForObject("select count(*) from MARIAGE", Long.class));
		Assert.assertEquals((Long) 6L, jdbc.queryForObject("select count(*) from ETAPE", Long.class));
		Assert.assertEquals((Long) 10L, jdbc.queryForObject("select count(*) from INVITE", Long.class));
	}

	@Test
	public void test02ChargeMariageParId() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		final Mariage charge = this.instance.chargerMariageParId(idMariage);

		// ASSERT
		Assert.assertEquals(original.getMarie1(), charge.getMarie1());
		Assert.assertEquals(original.getMarie2(), charge.getMarie2());
		Assert.assertEquals(original.getDateCelebration(), charge.getDateCelebration());
	}

	@Test
	public void test03ListeTousMariages01SansAutorisation() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		this.instance.sauvegarderGrappe(original);

		// ACT
		final Collection<Mariage> mariages = this.instance.listerMariagesAutorises(null);

		// ASSERT
		Assert.assertEquals(0, mariages.size());
	}

	@Test
	public void test03ListeTousMariages02AvecAutorisations() throws ParseException {

		// ARRANGE
		final String login = "monLogin";
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		this.securiteInstance.sauvegarderUtilisateur(login, "azeazeaze", Role.UTILISATEUR);
		this.securiteInstance.ajouterAutorisation(login, idMariage);

		// ACT
		final Collection<Mariage> mariages = this.instance.listerMariagesAutorises(login);

		// ASSERT
		Assert.assertEquals(1, mariages.size());
	}

	@Test
	public void test04SupprimeMariage01Seul() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSeul();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		this.instance.supprimerMariage(idMariage);

		// ASSERT
		Assert.assertNull(this.instance.chargerMariageParId(idMariage));
	}

	@Test
	public void test04SupprimeMariage02Simple() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		this.instance.supprimerMariage(idMariage);

		// ASSERT
		Assert.assertNull(this.instance.chargerMariageParId(idMariage));
	}

	@Test
	public void test04SupprimeMariage03Riche() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Etape etape = this.instance.listerEtapesParIdMariage(idMariage).iterator().next();
		;
		final Invite invite = this.instance.listerInvitesParIdMariage(idMariage).iterator().next();
		this.instance.sauvegarder(idMariage, new Presence(etape, invite, true, true, ""));

		// ACT
		this.instance.supprimerMariage(idMariage);

		// ASSERT
		Assert.assertNull(this.instance.chargerMariageParId(idMariage));
	}
}
