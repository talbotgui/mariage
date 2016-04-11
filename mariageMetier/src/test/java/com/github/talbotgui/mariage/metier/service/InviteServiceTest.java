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

import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.comparator.InviteComparator;
import com.github.talbotgui.mariage.metier.exception.BaseException;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.github.talbotgui.mariage.metier.service.MariageService;
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
		final Long id = this.instance.sauvegarde(idMariage, new Invite(groupe, nom, prenom, age));

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
}
