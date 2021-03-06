package com.github.talbotgui.mariage.metier.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesRepartitionsInvitesMariage;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.Presence;
import com.github.talbotgui.mariage.metier.entities.comparator.InviteComparator;
import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.BusinessException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InviteEtFoyerServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(InviteEtFoyerServiceTest.class);

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
		LOG.info("Execute SQL : {}", strings.toString());
		jdbc.batchUpdate(requetes);

	}

	@Test
	public void test01ListeInvitesParIdMariage() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		final Page<Invite> page1 = this.instance.listerInvitesParIdMariage(idMariage, new PageRequest(0, 2));
		final Page<Invite> page2 = this.instance.listerInvitesParIdMariage(idMariage, page1.nextPageable());
		final Page<Invite> page3 = this.instance.listerInvitesParIdMariage(idMariage, page2.nextPageable());
		final Page<Invite> page4 = this.instance.listerInvitesParIdMariage(idMariage, page3.nextPageable());
		final Page<Invite> page5 = this.instance.listerInvitesParIdMariage(idMariage, page4.nextPageable());

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
		for (final Invite i : page1.getContent()) {
			Assert.assertNotNull(i.getFoyer());
		}
	}

	@Test
	public void test02SauvegarderInvite01Creation() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Invite> inviteAvant = this.instance.listerInvitesParIdMariage(idMariage);

		// ACT
		final String nom = "N1";
		final String prenom = "P1";
		final Age age = Age.ADULTE;
		final String foyer = "foyer";
		final String groupe = "groupe";
		final String adresse = "adresse";
		final Long id = this.instance.sauvegarderInviteEtFoyer(idMariage,
				new Invite(null, nom, prenom, age, new Foyer(groupe, foyer, adresse, null, null)));

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Invite> inviteApres = this.instance.listerInvitesParIdMariage(idMariage);
		Assert.assertEquals(inviteAvant.size() + 1, inviteApres.size());
		final Collection<Invite> diff = new TreeSet<>(new InviteComparator());
		diff.addAll(inviteApres);
		diff.removeAll(inviteAvant);
		Assert.assertEquals(1, diff.size());
		final Invite nouvelInvite = diff.iterator().next();
		Assert.assertEquals(nom, nouvelInvite.getNom());
		Assert.assertEquals(prenom, nouvelInvite.getPrenom());
		Assert.assertEquals(age, nouvelInvite.getAge());
		Assert.assertEquals(foyer, nouvelInvite.getFoyer().getNom());
		Assert.assertEquals(groupe, nouvelInvite.getFoyer().getGroupe());
		Assert.assertEquals(adresse, nouvelInvite.getFoyer().getAdresse());
	}

	@Test
	public void test02SauvegarderInvite02FoyerExistant() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String nom = "N1";
		final String prenom = "P1";
		final Age age = Age.ADULTE;
		final String foyer = "foyer";
		final String groupe = "groupe";
		final String adresse = "adresse";
		final Long idPremierInvite = this.instance.sauvegarderInviteEtFoyer(idMariage,
				new Invite(null, nom, prenom, age, new Foyer(groupe, foyer, adresse, null, null)));
		final Invite premierInvite = this.instance.chargerInviteParId(idPremierInvite);
		final Collection<Invite> inviteAvant = this.instance.listerInvitesParIdMariage(idMariage);

		// ACT
		final String prenom2 = "P2";
		final Age age2 = Age.ADULTE;
		final Long id = this.instance.sauvegarderInviteEtFoyer(idMariage,
				new Invite(null, nom, prenom2, age2, new Foyer(premierInvite.getFoyer().getId())));

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Invite> inviteApres = this.instance.listerInvitesParIdMariage(idMariage);
		Assert.assertEquals(inviteAvant.size() + 1, inviteApres.size());
		final Collection<Invite> diff = new TreeSet<>(new InviteComparator());
		diff.addAll(inviteApres);
		diff.removeAll(inviteAvant);
		Assert.assertEquals(1, diff.size());
		final Invite nouvelInvite = diff.iterator().next();
		Assert.assertEquals(nom, nouvelInvite.getNom());
		Assert.assertEquals(prenom2, nouvelInvite.getPrenom());
		Assert.assertEquals(age2, nouvelInvite.getAge());
		Assert.assertEquals(foyer, nouvelInvite.getFoyer().getNom());
		Assert.assertEquals(groupe, nouvelInvite.getFoyer().getGroupe());
		Assert.assertEquals(adresse, nouvelInvite.getFoyer().getAdresse());
	}

	@Test
	public void test03SupprimeInvite() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Invite> inviteAvant = this.instance.listerInvitesParIdMariage(idMariage);

		// ACT
		final Iterator<Invite> iter = inviteAvant.iterator();
		this.instance.supprimerInvite(idMariage, iter.next().getId());
		this.instance.supprimerInvite(idMariage, iter.next().getId());
		this.instance.supprimerInvite(idMariage, iter.next().getId());

		// ASSERT
		final Collection<Invite> inviteApres = this.instance.listerInvitesParIdMariage(idMariage);
		Assert.assertEquals(inviteAvant.size() - 3, inviteApres.size());
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final Long nbFoyer = jdbc.queryForObject("select count(*) from Foyer", Long.class);
		Assert.assertEquals((Long) 2L, nbFoyer);
	}

	@Test
	public void test03SupprimeInviteAvecPresence() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Invite invite = this.instance.listerInvitesParIdMariage(idMariage).iterator().next();
		final Etape etape = this.instance.listerEtapesParIdMariage(idMariage).iterator().next();
		this.instance.sauvegarder(idMariage, new Presence(etape, invite, true, true, ""));

		// ACT
		this.instance.supprimerInvite(idMariage, invite.getId());

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final Long nbPresence = jdbc.queryForObject("select count(*) from PRESENCE", Long.class);
		Assert.assertEquals((Long) 0L, nbPresence);
	}

	@Test
	public void test03SupprimeInviteKo() throws ParseException {

		// ARRANGE

		// ACT
		final Throwable thrown = Assertions.catchThrowable(() -> {
			this.instance.supprimerInvite(-1L, -1L);
		});

		// ASSERT
		Assert.assertNotNull(thrown);
		Assert.assertEquals(BusinessException.class, thrown.getClass());
		Assert.assertTrue(BaseException.equals((Exception) thrown, BusinessException.ERREUR_ID_MARIAGE));
	}

	@Test
	public void test04ListeAges() {
		//

		//
		final Collection<String> ages = this.instance.listerAgePossible();

		//
		Assert.assertNotNull(ages);
		Assert.assertEquals(Arrays.asList(Age.values()).toString(), ages.toString());

	}

	@Test
	public void test05ModifieInvitation01Ajout() throws ParseException {

		// ARRANGE
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		final String sql = "select count(*) from INVITATION";
		final Long nbPresenceTrueAvant = jdbc.queryForObject(sql, Long.class);

		final Collection<Invite> invites = this.instance.listerInvitesParIdMariage(idMariage);
		final Invite invite = invites.iterator().next();
		final Collection<Courrier> courriers = this.instance.listerCourriersParIdMariage(idMariage);
		final Courrier courrier = courriers.iterator().next();

		// ACT
		this.instance.lierUnFoyerEtUnCourrier(idMariage, courrier.getId(), invite.getFoyer().getId(), true);

		// ASSERT
		final Long nbPresenceTrueApres = jdbc.queryForObject(sql, Long.class);
		Assert.assertEquals((Long) (nbPresenceTrueAvant + 1), nbPresenceTrueApres);
	}

	@Test
	public void test05ModifieInvitation02Retire() throws ParseException {

		// ARRANGE
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		final String sql = "select count(*) from INVITATION";
		final Long nbPresenceTrueAvant = jdbc.queryForObject(sql, Long.class);

		final Collection<Invite> invites = this.instance.listerInvitesParIdMariage(idMariage);
		final Invite invite = invites.iterator().next();
		final Collection<Courrier> courriers = this.instance.listerCourriersParIdMariage(idMariage);
		final Courrier courrier = courriers.iterator().next();

		this.instance.lierUnFoyerEtUnCourrier(idMariage, courrier.getId(), invite.getFoyer().getId(), true);

		// ACT
		this.instance.lierUnFoyerEtUnCourrier(idMariage, courrier.getId(), invite.getFoyer().getId(), false);

		// ASSERT
		final Long nbPresenceTrueApres = jdbc.queryForObject(sql, Long.class);
		Assert.assertEquals((nbPresenceTrueAvant), nbPresenceTrueApres);
	}

	@Test
	public void test07SauvegarderInvitesEnMasse() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Invite> inviteAvant = this.instance.listerInvitesParIdMariage(idMariage);
		final Collection<Invite> invitesAinserer = Arrays.asList(
				new Invite(null, "nom1", "prenom1", Age.ADULTE, new Foyer("foyer1")),
				new Invite(null, "nom2", "prenom2", Age.ADULTE, new Foyer("foyer1")),
				new Invite(null, "nom3", "prenom3", Age.ADULTE, new Foyer("foyer2")));

		// ACT
		this.instance.sauvegarderEnMasse(idMariage, invitesAinserer);

		// ASSERT
		final Collection<Invite> inviteApres = this.instance.listerInvitesParIdMariage(idMariage);
		Assert.assertEquals(inviteAvant.size() + invitesAinserer.size(), inviteApres.size());
		final Collection<Invite> diff = new TreeSet<>(new InviteComparator());
		diff.addAll(inviteApres);
		diff.removeAll(inviteAvant);
		Assert.assertEquals(invitesAinserer.size(), diff.size());
		for (final Invite i : diff) {
			Assert.assertNotNull(i.getFoyer());
		}
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
		final StatistiquesMariage dto = this.instance.calculerStatistiques(idMariage);

		// ASSERT
		Assert.assertNotNull(dto);
		final StatistiquesInvitesMariage invites = dto.getInvites();
		final StatistiquesRepartitionsInvitesMariage repartitions = dto.getRepartitions();

		Assert.assertEquals("invites", (Integer) 12, invites.getNbInvites());
		Assert.assertEquals("foyers", (Integer) 3, invites.getNbFoyers());
		Assert.assertEquals("groupes", (Integer) 2, invites.getNbGroupes());

		Assert.assertEquals("invites incomplets", (Integer) 1, invites.getNbInvitesIncomplets());
		Assert.assertEquals("invites sans adresse", (Integer) 4, invites.getNbInvitesSansAdresse());
		Assert.assertEquals("invites sans age", (Integer) 3, invites.getNbInvitesSansAge());

		Assert.assertEquals("invites par age nb", 6, repartitions.getNbParAge().size());
		Assert.assertEquals("invites par age value", (Integer) 1, repartitions.getNbParAge().get(Age.BEBE.toString()));
		Assert.assertEquals("invites par age value", (Integer) 3, repartitions.getNbParAge().get(""));

		Assert.assertEquals("invites par groupe nb", 2, repartitions.getNbParGroupe().size());
		Assert.assertEquals("invites par groupe value", (Integer) 8, repartitions.getNbParGroupe().get("GROUPE1"));

		Assert.assertEquals("invites par foyer nb", 3, repartitions.getNbParFoyer().size());
		Assert.assertEquals("invites par foyer value", (Integer) 4, repartitions.getNbParFoyer().get("FOYER1"));

		Assert.assertEquals("invites par etape nb", 5, repartitions.getNbParEtape().size());
		Assert.assertEquals("invites par etape value", (Integer) 12, repartitions.getNbParEtape().get("Mairie"));
		Assert.assertEquals("invites par etape value", (Integer) 4, repartitions.getNbParEtape().get("Repas"));

		Assert.assertEquals("foyers par etape nb", 5, repartitions.getNbFoyersParEtape().size());
		Assert.assertEquals("foyers par etape value", (Integer) 3, repartitions.getNbFoyersParEtape().get("Mairie"));
		Assert.assertEquals("foyers par etape value", (Integer) 1, repartitions.getNbFoyersParEtape().get("Repas"));
	}

	@Test
	public void test08ChargerInvite() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String nom = "N1";
		final String prenom = "P1";
		final Age age = Age.ADULTE;
		final String foyer = "foyer";
		final Long id = this.instance.sauvegarderInviteEtFoyer(idMariage,
				new Invite(null, nom, prenom, age, new Foyer(foyer)));

		// ACT
		final Invite invite = this.instance.chargerInviteParId(id);

		// ASSERT
		Assert.assertNotNull(invite);
		Assert.assertEquals(nom, invite.getNom());
		Assert.assertEquals(prenom, invite.getPrenom());
		Assert.assertEquals(age, invite.getAge());
		Assert.assertEquals(foyer, invite.getFoyer().getNom());
	}

	@Test
	public void test09ListeFoyers() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		final Collection<Foyer> foyers = this.instance.listerFoyersParIdMariage(idMariage);

		// ASSERT
		Assert.assertNotNull(foyers);
		Assert.assertEquals(3, foyers.size());
		Assert.assertEquals("F1", foyers.iterator().next().getNom());
	}

	@Test
	public void test10GetFoyer() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String nomFoyer = "F1";

		// ACT
		final Foyer foyer = this.instance.chargerFoyer(idMariage, nomFoyer);

		// ASSERT
		Assert.assertNotNull(foyer);
		Assert.assertEquals(nomFoyer, foyer.getNom());
	}

	@Test
	public void test11ChargeFoyerParId() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Long idFoyer = this.instance.listerFoyersParIdMariage(idMariage).iterator().next().getId();

		// ACT
		final Foyer foyer = this.instance.chargerFoyerParId(idFoyer);

		// ASSERT
		Assert.assertNotNull(foyer);
		Assert.assertEquals("F1", foyer.getNom());
	}

	@Test
	public void test11Publipostage() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageAvecInvitations();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Long idCourrier1 = this.instance.listerCourriersParIdMariage(idMariage).iterator().next().getId();

		// ACT
		final String contenuPublipostage = this.instance.genererPublipostage(idMariage, idCourrier1);

		// ASSERT
		Assert.assertNotNull(contenuPublipostage);
		final String[] lignes = contenuPublipostage.split("\n");
		Assert.assertEquals(4, lignes.length);
		Assert.assertEquals("NOM;RUE;VILLE", lignes[0]);
		Assert.assertEquals("G A X et G C X et G F X;add1", lignes[1]);
		Assert.assertEquals("G I X et G J X et T P X;add1", lignes[2]);
		Assert.assertEquals("T A X et T J X;add1", lignes[3]);
	}

	@Test
	public void test12StatistiquesPresence() throws ParseException, IOException, URISyntaxException {

		// ARRANGE
		final Collection<String> strings = Files
				.readAllLines(Paths.get(ClassLoader.getSystemResource("sql/dataSet_2.sql").toURI()));
		final String[] requetes = strings.toArray(new String[strings.size()]);
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.batchUpdate(requetes);

		final Long idMariage = jdbc.queryForObject("select ID from MARIAGE", Long.class);
		final Long idEtape = jdbc.queryForObject("select ID from ETAPE limit 1", Long.class);

		// ACT
		final Collection<StatistiquesPresenceMariage> dtos = this.instance.calculerStatistiquesPresence(idMariage,
				idEtape);

		// ASSERT
		Assert.assertEquals(6, dtos.size());
		final Iterator<StatistiquesPresenceMariage> iter = dtos.iterator();

		StatistiquesPresenceMariage stat = iter.next();
		Assert.assertEquals(idEtape, stat.getIdEtape());
		Assert.assertNull(stat.getNomAge());
		Assert.assertEquals(0, stat.getNbAbsence());
		Assert.assertEquals(0, stat.getNbAbsenceConfirme());
		Assert.assertEquals(0, stat.getNbPresence());
		Assert.assertEquals(0, stat.getNbPresenceConfirme());
		Assert.assertEquals(0, stat.getNbInconnu());

		stat = iter.next();
		Assert.assertEquals(idEtape, stat.getIdEtape());
		Assert.assertEquals(Age.ADULTE.name(), stat.getNomAge());
		Assert.assertEquals(1, stat.getNbAbsence());
		Assert.assertEquals(0, stat.getNbAbsenceConfirme());
		Assert.assertEquals(0, stat.getNbInconnu());
		Assert.assertEquals(2, stat.getNbPresence());
		Assert.assertEquals(1, stat.getNbPresenceConfirme());

		stat = iter.next();
		Assert.assertEquals(idEtape, stat.getIdEtape());
		Assert.assertEquals(Age.AINE.name(), stat.getNomAge());
		Assert.assertEquals(0, stat.getNbAbsence());
		Assert.assertEquals(0, stat.getNbAbsenceConfirme());
		Assert.assertEquals(1, stat.getNbInconnu());
		Assert.assertEquals(1, stat.getNbPresence());
		Assert.assertEquals(1, stat.getNbPresenceConfirme());

		stat = iter.next();
		Assert.assertEquals(idEtape, stat.getIdEtape());
		Assert.assertEquals(Age.BEBE.name(), stat.getNomAge());
		Assert.assertEquals(0, stat.getNbAbsence());
		Assert.assertEquals(0, stat.getNbAbsenceConfirme());
		Assert.assertEquals(0, stat.getNbInconnu());
		Assert.assertEquals(1, stat.getNbPresence());
		Assert.assertEquals(1, stat.getNbPresenceConfirme());

		stat = iter.next();
		Assert.assertEquals(idEtape, stat.getIdEtape());
		Assert.assertEquals(Age.ENFANT.name(), stat.getNomAge());
		Assert.assertEquals(0, stat.getNbAbsence());
		Assert.assertEquals(0, stat.getNbAbsenceConfirme());
		Assert.assertEquals(0, stat.getNbInconnu());
		Assert.assertEquals(1, stat.getNbPresence());
		Assert.assertEquals(1, stat.getNbPresenceConfirme());

		stat = iter.next();
		Assert.assertEquals(idEtape, stat.getIdEtape());
		Assert.assertEquals(Age.JEUNE.name(), stat.getNomAge());
		Assert.assertEquals(0, stat.getNbAbsence());
		Assert.assertEquals(0, stat.getNbAbsenceConfirme());
		Assert.assertEquals(0, stat.getNbInconnu());
		Assert.assertEquals(2, stat.getNbPresence());
		Assert.assertEquals(0, stat.getNbPresenceConfirme());
	}

	@Test
	public void test13RechercheDerreurs() throws ParseException, IOException, URISyntaxException {

		// ARRANGE
		final Collection<String> strings = Files
				.readAllLines(Paths.get(ClassLoader.getSystemResource("sql/dataSet_3.sql").toURI()));
		final String[] requetes = strings.toArray(new String[strings.size()]);
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		jdbc.batchUpdate(requetes);

		final Long idMariage = jdbc.queryForObject("select id from mariage", Long.class);

		// ACT
		final Collection<String> erreurs = this.instance.rechercherErreurs(idMariage);

		// ASSERT
		Assert.assertNotNull(erreurs);
		Assert.assertEquals(6, erreurs.size());
		final Iterator<String> iter = erreurs.iterator();
		Assert.assertEquals("PRENOM1 NOM1 est invité(e) plusieurs fois à une même étape : 'Mairie', 'Eglise', 'VdH'",
				iter.next());
		Assert.assertEquals("PRENOM2 NOM1 est invité(e) plusieurs fois à une même étape : 'Mairie', 'Eglise', 'VdH'",
				iter.next());
		Assert.assertEquals("PRENOM3 null est invité(e) plusieurs fois à une même étape : 'Mairie', 'Eglise', 'VdH'",
				iter.next());
		Assert.assertEquals("PRENOM4 NOM1 est invité(e) plusieurs fois à une même étape : 'Mairie', 'Eglise', 'VdH'",
				iter.next());
		Assert.assertEquals(
				"PRENOM1 NOM4 est marqué(e) présent/absent, sans plus y être invité(e), à l'étape 'Repas', 'En plus'",
				iter.next());
		Assert.assertEquals(
				"PRENOM2 NOM4 est marqué(e) présent/absent, sans plus y être invité(e), à l'étape 'En plus'",
				iter.next());
	}

	@Test
	public void test14ModificationInviteAvecInformationSoiree() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final Collection<Invite> invitesAvant = this.instance.listerInvitesParIdMariage(idMariage);
		final Invite invite = invitesAvant.iterator().next();

		// ACT
		final String commentaire = "MonCommentaire";
		final Boolean participantAuxAnimations = true;
		final Boolean particularite = false;
		invite.setCommentaire(commentaire);
		invite.setParticipantAuxAnimations(participantAuxAnimations);
		invite.setParticularite(particularite);
		final Long id = this.instance.sauvegarderInviteEtFoyer(idMariage, invite);

		// ASSERT
		Assert.assertNotNull(id);
		final Collection<Invite> invitesApres = this.instance.listerInvitesParIdMariage(idMariage);
		Assert.assertEquals(invitesAvant.size(), invitesApres.size());
		final Invite inviteApres = invitesApres.iterator().next();
		Assert.assertEquals(commentaire, inviteApres.getCommentaire());
		Assert.assertEquals(participantAuxAnimations, inviteApres.getParticipantAuxAnimations());
		Assert.assertEquals(particularite, inviteApres.getParticularite());
	}

	@Test
	public void test15ListeInvitesPresent() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageAvecInvitations();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		final Collection<Presence> presencesDepuisProduitCartesien = this.instance
				.listerPresencesParIdMariage(idMariage);

		final Presence presence = presencesDepuisProduitCartesien.iterator().next();
		presence.setCommentaire("coucou");
		presence.setConfirmee(true);
		presence.setPresent(true);
		this.instance.sauvegarder(idMariage, presence);

		// ACT
		final Collection<Invite> invitesPresents = this.instance.listerInvitesPresentsParIdMariage(idMariage);

		// ASSERT
		Assert.assertEquals(1, invitesPresents.size());
		Assert.assertEquals(presence.getId().getInvite().getId(), invitesPresents.iterator().next().getId());
	}
}
