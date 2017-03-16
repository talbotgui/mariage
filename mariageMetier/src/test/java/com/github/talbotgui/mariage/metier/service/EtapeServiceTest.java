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

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.Presence;
import com.github.talbotgui.mariage.metier.entities.comparator.EtapeComparator;
import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.BusinessException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringApplicationForTests.class)
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
		final Collection<String> strings = Files
				.readAllLines(Paths.get(ClassLoader.getSystemResource("sql/dataPurge.sql").toURI()));
		final String[] requetes = strings.toArray(new String[strings.size()]);
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		LOG.info("Execute SQL : {}", (Object[]) requetes);
		jdbc.batchUpdate(requetes);

	}

	@Test
	public void test01ListeEtapesParIdMariage() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		final Collection<Etape> etapes = this.instance.listerEtapesParIdMariage(idMariage);

		// ASSERT
		Assert.assertEquals(6, etapes.size());
	}

	@Test
	public void test02SauvegarderEtapeCeremonie() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Etape> etapeAvant = this.instance.listerEtapesParIdMariage(idMariage);

		// ACT
		final String lieu = "G1";
		final String nom = "N1";
		final Date date = new Date();
		final String celebrant = "Maire";
		final Integer numOrdre = 1;
		final Long id = this.instance.sauvegarder(idMariage, new EtapeCeremonie(numOrdre, nom, date, lieu, celebrant));

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Etape> etapeApres = this.instance.listerEtapesParIdMariage(idMariage);
		Assert.assertEquals(etapeAvant.size() + 1, etapeApres.size());
		final Collection<Etape> diff = new TreeSet<>(new EtapeComparator());
		diff.addAll(etapeApres);
		diff.removeAll(etapeAvant);
		Assert.assertEquals(1, diff.size());
		Assert.assertEquals(numOrdre, diff.iterator().next().getNumOrdre());
		Assert.assertEquals(nom, diff.iterator().next().getNom());
		Assert.assertEquals(lieu, diff.iterator().next().getLieu());
		Assert.assertEquals(date, diff.iterator().next().getDateHeure());
		Assert.assertEquals(EtapeCeremonie.class, diff.iterator().next().getClass());
		Assert.assertEquals(celebrant, ((EtapeCeremonie) diff.iterator().next()).getCelebrant());
	}

	@Test
	public void test02SauvegarderEtapeRepas() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Etape> etapeAvant = this.instance.listerEtapesParIdMariage(idMariage);

		// ACT
		final String lieu = "G1";
		final String nom = "N1";
		final Date date = new Date();
		final Integer numOrdre = 1;
		final Long id = this.instance.sauvegarder(idMariage, new EtapeRepas(numOrdre, nom, date, lieu));

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Etape> etapeApres = this.instance.listerEtapesParIdMariage(idMariage);
		Assert.assertEquals(etapeAvant.size() + 1, etapeApres.size());
		final Collection<Etape> diff = new TreeSet<>(new EtapeComparator());
		diff.addAll(etapeApres);
		diff.removeAll(etapeAvant);
		Assert.assertEquals(1, diff.size());
		Assert.assertEquals(nom, diff.iterator().next().getNom());
		Assert.assertEquals(numOrdre, diff.iterator().next().getNumOrdre());
		Assert.assertEquals(lieu, diff.iterator().next().getLieu());
		Assert.assertEquals(date, diff.iterator().next().getDateHeure());
		Assert.assertEquals(EtapeRepas.class, diff.iterator().next().getClass());
	}

	@Test
	public void test02SauvegarderEtapeRepasSansNumOrdre() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Etape> etapeAvant = this.instance.listerEtapesParIdMariage(idMariage);

		// ACT
		final String lieu = "G1";
		final String nom = "N1";
		final Date date = new Date();
		final Integer numOrdreFourni = null;
		final Integer numOrdreAttendu = original.getEtapes().size() + 1;
		final Long id = this.instance.sauvegarder(idMariage, new EtapeRepas(numOrdreFourni, nom, date, lieu));

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Etape> etapeApres = this.instance.listerEtapesParIdMariage(idMariage);
		Assert.assertEquals(etapeAvant.size() + 1, etapeApres.size());
		final Collection<Etape> diff = new TreeSet<>(new EtapeComparator());
		diff.addAll(etapeApres);
		diff.removeAll(etapeAvant);
		Assert.assertEquals(1, diff.size());
		Assert.assertEquals(nom, diff.iterator().next().getNom());
		Assert.assertEquals(numOrdreAttendu, diff.iterator().next().getNumOrdre());
		Assert.assertEquals(lieu, diff.iterator().next().getLieu());
		Assert.assertEquals(date, diff.iterator().next().getDateHeure());
		Assert.assertEquals(EtapeRepas.class, diff.iterator().next().getClass());
	}

	@Test
	public void test03SupprimeEtape() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Etape> etapeAvant = this.instance.listerEtapesParIdMariage(idMariage);
		for (final Courrier c : original.getCourriers()) {
			this.instance.supprimerCourrier(original.getId(), c.getId());
		}

		// ACT
		this.instance.supprimerEtape(idMariage, etapeAvant.iterator().next().getId());

		// ASSERT
		final Collection<Etape> etapeApres = this.instance.listerEtapesParIdMariage(idMariage);
		Assert.assertEquals(etapeAvant.size() - 1, etapeApres.size());
	}

	@Test
	public void test03SupprimeEtapeAvecPresences() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Etape etape = this.instance.listerEtapesParIdMariage(idMariage).iterator().next();
		final Invite invite = this.instance.listerInvitesParIdMariage(idMariage).iterator().next();
		final Courrier courrier = this.instance.listerCourriersParIdMariage(idMariage).iterator().next();
		this.instance.sauvegarder(idMariage, new Presence(etape, invite, true, true, ""));
		this.instance.supprimerCourrier(idMariage, courrier.getId());

		// ACT
		this.instance.supprimerEtape(idMariage, etape.getId());

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		Assert.assertEquals((Long) 0L, jdbc.queryForObject("select count(*) from PRESENCE", Long.class));
		Assert.assertEquals((Long) 0L, jdbc.queryForObject("select count(*) from ETAPE where id=?",
				new Object[] { etape.getId() }, Long.class));
	}

	@Test
	public void test03SupprimeEtapeKoCourrierLie() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Etape> etapeAvant = this.instance.listerEtapesParIdMariage(idMariage);

		// ACT
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.supprimerEtape(idMariage, etapeAvant.iterator().next().getId());
		});

		// ASSERT
		Assert.assertNotNull(thrown);
		Assert.assertEquals(BusinessException.class, thrown.getClass());
		Assert.assertTrue(BaseException.equals((Exception) thrown, BusinessException.ERREUR_COURRIER_LIE_A_ETAPE));
	}

	@Test
	public void test03SupprimeEtapeKoIdInvalide() throws ParseException {

		// ARRANGE

		// ACT
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.supprimerEtape(-1L, -1L);
		});

		// ASSERT
		Assert.assertNotNull(thrown);
		Assert.assertEquals(BusinessException.class, thrown.getClass());
		Assert.assertTrue(BaseException.equals((Exception) thrown, BusinessException.ERREUR_ID_MARIAGE));
	}

	@Test
	public void test04LieCourrierEtEtapeKo() throws ParseException {

		// ARRANGE
		final Mariage original1 = ObjectMother.creeMariageSimple();
		final Long idMariage1 = this.instance.sauvegarderGrappe(original1);
		final Mariage original2 = ObjectMother.creeMariageSimple();
		this.instance.sauvegarderGrappe(original2);

		// ACT
		final Long idCourrier = original1.getCourriers().iterator().next().getId();
		final Long idEtape = original2.getEtapes().iterator().next().getId();
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.lierUneEtapeEtUnCourrier(idMariage1, idEtape, idCourrier, true);
		});

		// ASSERT
		Assert.assertNotNull(thrown);
		Assert.assertEquals(BusinessException.class, thrown.getClass());
		Assert.assertTrue(BaseException.equals((Exception) thrown, BusinessException.ERREUR_ID_MARIAGE));
	}

	@Test
	public void test04LieCourrierEtEtapeOk() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		final Long idCourrier = original.getCourriers().iterator().next().getId();
		for (final Etape e : original.getEtapes()) {
			this.instance.lierUneEtapeEtUnCourrier(idMariage, e.getId(), idCourrier, true);
		}

		// ASSERT
		final Collection<Courrier> courriers = this.instance.listerCourriersParIdMariage(idMariage);
		final Courrier c = courriers.iterator().next();
		Assert.assertEquals(original.getEtapes().size(), c.getEtapes().size());
	}

	@Test
	public void test05DelieCourrierEtEtapeOk() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		final Long idCourrier = original.getCourriers().iterator().next().getId();
		for (final Etape e : original.getEtapes()) {
			this.instance.lierUneEtapeEtUnCourrier(idMariage, e.getId(), idCourrier, false);
		}

		// ASSERT
		final Collection<Courrier> courriers = this.instance.listerCourriersParIdMariage(idMariage);
		final Courrier c = courriers.iterator().next();
		Assert.assertEquals(0, c.getEtapes().size());
	}

}
