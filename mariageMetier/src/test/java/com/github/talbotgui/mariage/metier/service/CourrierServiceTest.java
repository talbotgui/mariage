package com.github.talbotgui.mariage.metier.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

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

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.comparator.CourrierComparator;
import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.googlecode.catchexception.CatchException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourrierServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(CourrierServiceTest.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MariageService instance;

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
	public void test01ListeCourriersParIdMariage() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);

		// ACT
		final Collection<Courrier> courriers = this.instance.listeCourriersParIdMariage(idMariage);

		// ASSERT
		Assert.assertEquals(2, courriers.size());
	}

	@Test
	public void test02SauvegarderCourrier() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);
		final Collection<Courrier> courrierAvant = this.instance.listeCourriersParIdMariage(idMariage);

		// ACT
		final String nom = "NouveauCourrier";
		final Date date = new Date();
		final Long id = this.instance.sauvegarde(idMariage, new Courrier(nom, date));

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Courrier> courrierApres = this.instance.listeCourriersParIdMariage(idMariage);
		Assert.assertEquals(courrierAvant.size() + 1, courrierApres.size());
		final Collection<Courrier> diff = new TreeSet<>(new CourrierComparator());
		diff.addAll(courrierApres);
		diff.removeAll(courrierAvant);
		Assert.assertEquals(1, diff.size());
		Assert.assertEquals(nom, diff.iterator().next().getNom());
		Assert.assertEquals(date, diff.iterator().next().getDatePrevisionEnvoi());
	}

	@Test
	public void test03SupprimeCourrier() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);
		final Collection<Courrier> courrierAvant = this.instance.listeCourriersParIdMariage(idMariage);

		// ACT
		this.instance.supprimeCourrier(idMariage, courrierAvant.iterator().next().getId());

		// ASSERT
		final Collection<Courrier> courrierApres = this.instance.listeCourriersParIdMariage(idMariage);
		Assert.assertEquals(courrierAvant.size() - 1, courrierApres.size());
	}

	@Test
	public void test03SupprimeCourrierKo() throws ParseException {

		// ARRANGE

		// ACT
		CatchException.catchException(this.instance).supprimeCourrier(-1L, -1L);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertEquals(BusinessException.class, CatchException.caughtException().getClass());
		Assert.assertTrue(BaseException.equals(CatchException.caughtException(), BusinessException.ERREUR_ID_MARIAGE));
	}

}
