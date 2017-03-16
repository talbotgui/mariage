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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.Presence;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PresenceServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(PresenceServiceTest.class);

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
	public void test01SauvegardePresence() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageAvecInvitations();
		final Long idMariage = this.instance.sauvegarderGrappe(original);
		final String commentaire = "commentaireDeLaPresence";
		final Etape etape = original.getEtapes().iterator().next();
		final Invite invite = original.getFoyers().iterator().next().getInvites().iterator().next();
		final Presence presence = new Presence(etape, invite);
		presence.setCommentaire(commentaire);
		presence.setConfirmee(true);
		presence.setPresent(true);

		// ACT
		this.instance.sauvegarder(idMariage, presence);

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		final Long nbPresence = jdbc.queryForObject("select count(*) from PRESENCE", Long.class);
		final Long nbPresenceAvecBoolean = jdbc.queryForObject(
				"select count(*) from PRESENCE where CONFIRMEE=true and PRESENT=true and DATE_MAJ is not null",
				Long.class);
		final String commentaireObtenu = jdbc.queryForObject("select COMMENTAIRE from PRESENCE ", String.class);

		Assert.assertEquals(new Long(1), nbPresence);
		Assert.assertEquals(new Long(1), nbPresenceAvecBoolean);
		Assert.assertEquals(commentaireObtenu, commentaire);
	}

	@Test
	public void test02ListePresence01AucuneSauvegardee() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageAvecInvitations();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		// ACT
		final Collection<Presence> presences = this.instance.listerPresencesParIdMariage(idMariage);

		// ASSERT
		Assert.assertEquals(48, presences.size());
		for (final Presence presence : presences) {
			Assert.assertNull(presence.getCommentaire());
			Assert.assertNull(presence.getConfirmee());
			Assert.assertNull(presence.getDateMaj());
			Assert.assertNull(presence.getPresent());
		}
	}

	@Test
	public void test02ListePresence02AvecUnePresenceEnBase() throws ParseException {

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
		final Collection<Presence> presences = this.instance.listerPresencesParIdMariage(idMariage);

		// ASSERT
		Assert.assertEquals(presencesDepuisProduitCartesien.size(), presences.size());
		for (final Presence p : presences) {
			if (p.getId().equals(presence.getId())) {
				Assert.assertNotNull(p.getCommentaire());
				Assert.assertTrue(p.getConfirmee());
				Assert.assertNotNull(p.getDateMaj());
				Assert.assertTrue(p.getPresent());
			} else {
				Assert.assertNull(p.getCommentaire());
				Assert.assertNull(p.getConfirmee());
				Assert.assertNull(p.getDateMaj());
				Assert.assertNull(p.getPresent());
			}
		}
	}

	@Test
	public void test03ChargePresenceParEtapeEtInvite() throws ParseException {

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
		final Presence presenceChargee = this.instance.chargerPresenceParEtapeEtInvite(idMariage,
				presence.getId().getEtape().getId(), presence.getId().getInvite().getId());

		// ASSERT
		Assert.assertEquals(presence.getId(), presenceChargee.getId());
		Assert.assertNotNull(presence.getCommentaire());
		Assert.assertTrue(presence.getConfirmee());
		Assert.assertNotNull(presence.getDateMaj());
		Assert.assertTrue(presence.getPresent());

	}

	@Test
	public void test04SupprimePresence01NonExistant() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageAvecInvitations();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		Collection<Presence> presencesDepuisProduitCartesien = this.instance.listerPresencesParIdMariage(idMariage);

		Presence presence = presencesDepuisProduitCartesien.iterator().next();

		// ACT
		this.instance.supprimerPresence(idMariage, presence.getId().getInvite().getId(),
				presence.getId().getEtape().getId());

		// ASSERT
		presencesDepuisProduitCartesien = this.instance.listerPresencesParIdMariage(idMariage);
		presence = presencesDepuisProduitCartesien.iterator().next();
		Assert.assertNull(presence.getCommentaire());
		Assert.assertNull(presence.getConfirmee());
		Assert.assertNull(presence.getDateMaj());
		Assert.assertNull(presence.getPresent());

	}

	@Test
	public void test04SupprimePresence02Existant() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageAvecInvitations();
		final Long idMariage = this.instance.sauvegarderGrappe(original);

		Collection<Presence> presencesDepuisProduitCartesien = this.instance.listerPresencesParIdMariage(idMariage);

		Presence presence = presencesDepuisProduitCartesien.iterator().next();
		presence.setCommentaire("coucou");
		presence.setConfirmee(true);
		presence.setPresent(true);
		this.instance.sauvegarder(idMariage, presence);

		// ACT
		this.instance.supprimerPresence(idMariage, presence.getId().getInvite().getId(),
				presence.getId().getEtape().getId());

		// ASSERT
		presencesDepuisProduitCartesien = this.instance.listerPresencesParIdMariage(idMariage);
		presence = presencesDepuisProduitCartesien.iterator().next();
		Assert.assertNull(presence.getCommentaire());
		Assert.assertNull(presence.getConfirmee());
		Assert.assertNull(presence.getDateMaj());
		Assert.assertNull(presence.getPresent());

	}

}
