package com.github.talbotgui.mariage.metier.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesRepartitionsInvitesMariage;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.PresenceEtape;
import com.github.talbotgui.mariage.metier.entities.comparator.InviteComparator;
import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.googlecode.catchexception.CatchException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InviteServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(InviteServiceTest.class);

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
		LOG.info("Execute SQL : {}", Arrays.asList(requetes));
		jdbc.batchUpdate(requetes);

	}

	@Test
	public void test01ListeInvitesParIdMariage() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);

		// ACT
		final Page<Invite> page1 = this.instance.listeInvitesParIdMariage(idMariage, new PageRequest(0, 2));
		final Page<Invite> page2 = this.instance.listeInvitesParIdMariage(idMariage, page1.nextPageable());
		final Page<Invite> page3 = this.instance.listeInvitesParIdMariage(idMariage, page2.nextPageable());
		final Page<Invite> page4 = this.instance.listeInvitesParIdMariage(idMariage, page3.nextPageable());
		final Page<Invite> page5 = this.instance.listeInvitesParIdMariage(idMariage, page4.nextPageable());

		// ASSERT
		Assert.assertEquals(10, page1.getTotalElements());
		Assert.assertEquals(10, page2.getTotalElements());
		Assert.assertEquals(10, page3.getTotalElements());
		Assert.assertEquals(10, page4.getTotalElements());
		Assert.assertEquals(10, page5.getTotalElements());
		Assert.assertEquals(2, page1.getSize());
		Assert.assertEquals(2, page2.getSize());
		Assert.assertEquals(2, page3.getSize());
		Assert.assertEquals(2, page4.getSize());
		Assert.assertEquals(2, page5.getSize());
	}

	@Test
	public void test02SauvegarderInvite() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);
		final Collection<Invite> inviteAvant = this.instance.listeInvitesParIdMariage(idMariage);

		// ACT
		final String groupe = "G1";
		final String nom = "N1";
		final String prenom = "P1";
		final Age age = Age.adulte;
		final Long id = this.instance.sauvegarde(idMariage, new Invite(null, groupe, nom, prenom, age));

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Invite> inviteApres = this.instance.listeInvitesParIdMariage(idMariage);
		Assert.assertEquals(inviteAvant.size() + 1, inviteApres.size());
		final Collection<Invite> diff = new TreeSet<>(new InviteComparator());
		diff.addAll(inviteApres);
		diff.removeAll(inviteAvant);
		Assert.assertEquals(1, diff.size());
		Assert.assertEquals(nom, diff.iterator().next().getNom());
		Assert.assertEquals(groupe, diff.iterator().next().getGroupe());
		Assert.assertEquals(prenom, diff.iterator().next().getPrenom());
		Assert.assertEquals(age, diff.iterator().next().getAge());
	}

	@Test
	public void test03SupprimeInvite() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);
		final Collection<Invite> inviteAvant = this.instance.listeInvitesParIdMariage(idMariage);

		// ACT
		this.instance.suprimeInvite(idMariage, inviteAvant.iterator().next().getId());

		// ASSERT
		final Collection<Invite> inviteApres = this.instance.listeInvitesParIdMariage(idMariage);
		Assert.assertEquals(inviteAvant.size() - 1, inviteApres.size());
	}

	@Test
	public void test03SupprimeInviteKo() throws ParseException {

		// ARRANGE

		// ACT
		CatchException.catchException(this.instance).suprimeInvite(-1L, -1L);

		// ASSERT
		Assert.assertNotNull(CatchException.caughtException());
		Assert.assertEquals(BusinessException.class, CatchException.caughtException().getClass());
		Assert.assertTrue(BaseException.equals(CatchException.caughtException(), BusinessException.ERREUR_ID_MARIAGE));
	}

	@Test
	public void test04ListeAges() {
		//

		//
		final Collection<String> ages = this.instance.listeAgePossible();

		//
		Assert.assertNotNull(ages);
		Assert.assertEquals(Arrays.asList(Age.values()).toString(), ages.toString());

	}

	@Test
	public void test05ModifiePresence() throws ParseException {

		// ARRANGE
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);

		final String sql = "select count(*) from presence_etape where present=true";
		final Long nbPresenceTrueAvant = jdbc.queryForObject(sql, Long.class);

		final Collection<Invite> invites = this.instance.listeInvitesParIdMariage(idMariage);
		final Invite invite = invites.iterator().next();
		final PresenceEtape pe = invite.getPresencesEtape().iterator().next();

		// ACT
		this.instance.modifiePresenceEtape(idMariage, pe.getId(), true);

		// ASSERT
		final Long nbPresenceTrueApres = jdbc.queryForObject(sql, Long.class);
		Assert.assertEquals((Long) (nbPresenceTrueAvant + 1), nbPresenceTrueApres);
	}

	@Test
	public void test07SauvegarderInvitesEnMasse() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);
		final Collection<Invite> inviteAvant = this.instance.listeInvitesParIdMariage(idMariage);
		final Collection<Invite> invitesAinserer = Arrays.asList(new Invite(null, "G1", "nom1", "prenom1", Age.adulte),
				new Invite(null, "G1", "nom2", "prenom2", Age.adulte));

		// ACT
		this.instance.sauvegardeEnMasse(idMariage, invitesAinserer);

		// ASSERT
		final Collection<Invite> inviteApres = this.instance.listeInvitesParIdMariage(idMariage);
		Assert.assertEquals(inviteAvant.size() + invitesAinserer.size(), inviteApres.size());
		final Collection<Invite> diff = new TreeSet<>(new InviteComparator());
		diff.addAll(inviteApres);
		diff.removeAll(inviteAvant);
		Assert.assertEquals(invitesAinserer.size(), diff.size());
	}

	@Test
	public void test07Statistiques() throws ParseException, IOException, URISyntaxException {

		// ARRANGE
		final Collection<String> strings = Files
				.readAllLines(Paths.get(ClassLoader.getSystemResource("sql/dataSet_1.sql").toURI()));
		final String[] requetes = strings.toArray(new String[strings.size()]);
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.batchUpdate(requetes);
		final Long idMariage = jdbc.queryForObject("select id from mariage", Long.class);

		// ACT
		final StatistiquesMariage dto = this.instance.calculStatistiques(idMariage);

		// ASSERT
		Assert.assertNotNull(dto);
		final StatistiquesInvitesMariage invites = dto.getInvites();
		final StatistiquesRepartitionsInvitesMariage repartitions = dto.getRepartitions();

		Assert.assertEquals("invites", (Integer) 12, invites.getNbInvites());
		Assert.assertEquals("foyers", (Integer) 3, invites.getNbFoyers());
		Assert.assertEquals("groupes", (Integer) 2, invites.getNbGroupes());

		Assert.assertEquals("invites incomplets", (Integer) 1, invites.getNbInvitesIncomplets());
		Assert.assertEquals("invites sans adresse", (Integer) 1, invites.getNbInvitesSansAdresse());
		Assert.assertEquals("invites sans age", (Integer) 3, invites.getNbInvitesSansAge());

		Assert.assertEquals("invites par age nb", 6, repartitions.getNbParAge().size());
		Assert.assertEquals("invites par age value", (Integer) 1, repartitions.getNbParAge().get(Age.bebe.toString()));
		Assert.assertEquals("invites par age value", (Integer) 3, repartitions.getNbParAge().get(""));

		Assert.assertEquals("invites par groupe nb", 2, repartitions.getNbParGroupe().size());
		Assert.assertEquals("invites par groupe value", (Integer) 8, repartitions.getNbParGroupe().get("GROUPE1"));

		Assert.assertEquals("invites par foyer nb", 3, repartitions.getNbParFoyer().size());
		Assert.assertEquals("invites par foyer value", (Integer) 4, repartitions.getNbParFoyer().get("FOYER1"));

		Assert.assertEquals("invites par etape nb", 4, repartitions.getNbParEtape().size());
		Assert.assertEquals("invites par etape value", (Integer) 8, repartitions.getNbParEtape().get("Mairie"));
	}

	@Test
	public void test08ChargerInvite() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);
		final String groupe = "G1";
		final String nom = "N1";
		final String prenom = "P1";
		final Age age = Age.adulte;
		final Long id = this.instance.sauvegarde(idMariage, new Invite(null, groupe, nom, prenom, age));

		// ACT
		final Invite invite = this.instance.chargeInviteParId(id);

		// ASSERT
		Assert.assertNotNull(invite);
		Assert.assertEquals(nom, invite.getNom());
		Assert.assertEquals(groupe, invite.getGroupe());
		Assert.assertEquals(prenom, invite.getPrenom());
		Assert.assertEquals(age, invite.getAge());
	}
}
