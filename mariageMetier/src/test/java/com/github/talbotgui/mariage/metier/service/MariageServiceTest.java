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

import com.github.talbotgui.mariage.metier.entities.Mariage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringApplicationForTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MariageServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(MariageServiceTest.class);

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
	public void test01CreationMariage() throws ParseException {

		// ARRANGE
		final Mariage mariage = ObjectMother.creeMariageSimple();

		// ACT
		this.instance.sauvegardeGrappe(mariage);

		// ASSERT
		final JdbcTemplate jdbc = new JdbcTemplate(this.dataSource);
		Assert.assertEquals(Long.valueOf(1), jdbc.queryForObject("select count(*) from MARIAGE", Long.class));
		Assert.assertEquals(Long.valueOf(6), jdbc.queryForObject("select count(*) from ETAPE", Long.class));
		Assert.assertEquals(Long.valueOf(10), jdbc.queryForObject("select count(*) from INVITE", Long.class));
		Assert.assertEquals(Long.valueOf(60), jdbc.queryForObject("select count(*) from PRESENCE_ETAPE", Long.class));
	}

	@Test
	public void test02ChargeMariageParId() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);

		// ACT
		final Mariage charge = this.instance.chargeMariageParId(idMariage);

		// ASSERT
		Assert.assertEquals(original.getMarie1(), charge.getMarie1());
		Assert.assertEquals(original.getMarie2(), charge.getMarie2());
		Assert.assertEquals(original.getDateCelebration(), charge.getDateCelebration());
	}

	@Test
	public void test03ListeTousMariages() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		this.instance.sauvegardeGrappe(original);

		// ACT
		final Collection<Mariage> mariages = this.instance.listeTousMariages();

		// ASSERT
		Assert.assertEquals(1, mariages.size());
	}

	@Test
	public void test04SupprimeMariage01Seul() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSeul();
		final Long idMariage = this.instance.sauvegardeGrappe(original);

		// ACT
		this.instance.suprimeMariage(idMariage);

		// ASSERT
		Assert.assertNull(this.instance.chargeMariageParId(idMariage));
	}

	@Test
	public void test04SupprimeMariage02Simple() throws ParseException {

		// ARRANGE
		final Mariage original = ObjectMother.creeMariageSimple();
		final Long idMariage = this.instance.sauvegardeGrappe(original);

		// ACT
		this.instance.suprimeMariage(idMariage);

		// ASSERT
		Assert.assertNull(this.instance.chargeMariageParId(idMariage));
	}
}
