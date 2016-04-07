package com.github.talbotgui.mariage.metier;

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

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.comparator.EtapeComparator;
import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.googlecode.catchexception.CatchException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EtapeServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(EtapeServiceTest.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MariageService instance;

	@Before
	public void before() throws IOException, URISyntaxException {
		LOG.info("---------------------------------------------------------");

		// Destruction des données
		Collection<String> strings = Files
				.readAllLines(Paths.get(ClassLoader.getSystemResource("sql/dataPurge.sql").toURI()));
		String[] requetes = strings.toArray(new String[strings.size()]);
		JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		LOG.info("Execute SQL : {}", (Object[]) requetes);
		jdbc.batchUpdate(requetes);

	}

	@Test
	public void test01ListeEtapesParIdMariage() throws ParseException {

		// ARRANGE
		Mariage original = ObjectMother.creeMariageSimple();
		Long idMariage = this.instance.sauvegardeGrappe(original);

		// ACT
		Collection<Etape> etapes = this.instance.listeEtapesParIdMariage(idMariage);

		// ASSERT
		Assert.assertEquals(6, etapes.size());
	}

	@Test
	public void test02SauvegarderEtapeCeremonie() throws ParseException {

		// ARRANGE
		Mariage original = ObjectMother.creeMariageSimple();
		Long idMariage = this.instance.sauvegardeGrappe(original);
		Collection<Etape> etapeAvant = this.instance.listeEtapesParIdMariage(idMariage);

		// ACT
		final String lieu = "G1";
		final String nom = "N1";
		final Date date = new Date();
		final String celebrant = "Maire";
		Long id = this.instance.sauvegarde(idMariage, new EtapeCeremonie(nom, date, lieu, celebrant));

		// ASSERT
		Assert.assertNotNull(id);
		Collection<Etape> etapeApres = this.instance.listeEtapesParIdMariage(idMariage);
		Assert.assertEquals(etapeAvant.size() + 1, etapeApres.size());
		Collection<Etape> diff = new TreeSet<>(new EtapeComparator());
		diff.addAll(etapeApres);
		diff.removeAll(etapeAvant);
		Assert.assertEquals(1, diff.size());
		Assert.assertEquals(nom, diff.iterator().next().getNom());
		Assert.assertEquals(lieu, diff.iterator().next().getLieu());
		Assert.assertEquals(date, diff.iterator().next().getDateHeure());
		Assert.assertEquals(EtapeCeremonie.class, diff.iterator().next().getClass());
		Assert.assertEquals(celebrant, ((EtapeCeremonie) diff.iterator().next()).getCelebrant());
	}

	@Test
	public void test02SauvegarderEtapeRepas() throws ParseException {

		// ARRANGE
		Mariage original = ObjectMother.creeMariageSimple();
		Long idMariage = this.instance.sauvegardeGrappe(original);
		Collection<Etape> etapeAvant = this.instance.listeEtapesParIdMariage(idMariage);

		// ACT
		final String lieu = "G1";
		final String nom = "N1";
		final Date date = new Date();
		Long id = this.instance.sauvegarde(idMariage, new EtapeRepas(nom, date, lieu));

		// ASSERT
		Assert.assertNotNull(id);
		Collection<Etape> etapeApres = this.instance.listeEtapesParIdMariage(idMariage);
		Assert.assertEquals(etapeAvant.size() + 1, etapeApres.size());
		Collection<Etape> diff = new TreeSet<>(new EtapeComparator());
		diff.addAll(etapeApres);
		diff.removeAll(etapeAvant);
		Assert.assertEquals(1, diff.size());
		Assert.assertEquals(nom, diff.iterator().next().getNom());
		Assert.assertEquals(lieu, diff.iterator().next().getLieu());
		Assert.assertEquals(date, diff.iterator().next().getDateHeure());
		Assert.assertEquals(EtapeRepas.class, diff.iterator().next().getClass());
	}

	@Test
	public void test03SupprimeEtape() throws ParseException {

		// ARRANGE
		Mariage original = ObjectMother.creeMariageSimple();
		Long idMariage = this.instance.sauvegardeGrappe(original);
		Collection<Etape> etapeAvant = this.instance.listeEtapesParIdMariage(idMariage);

		// ACT
		this.instance.suprimeEtape(idMariage, etapeAvant.iterator().next().getId());

		// ASSERT
		Collection<Etape> etapeApres = this.instance.listeEtapesParIdMariage(idMariage);
		Assert.assertEquals(etapeAvant.size() - 1, etapeApres.size());
	}

	@Test
	public void test03SupprimeEtapeKo() throws ParseException {

		// ARRANGE

		// ACT
		CatchException.catchException(this.instance).suprimeEtape(-1L, -1L);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertEquals(BusinessException.class, CatchException.caughtException().getClass());
		Assert.assertTrue(BaseException.equals(CatchException.caughtException(), BusinessException.ERREUR_ID_MARIAGE));
	}

}
