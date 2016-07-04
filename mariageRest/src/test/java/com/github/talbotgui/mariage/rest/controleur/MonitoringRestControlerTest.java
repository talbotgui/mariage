package com.github.talbotgui.mariage.rest.controleur;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MonitoringRestControlerTest extends BaseRestControlerTest {

	@Test
	public void test01GetMonitoring() {

		// ARRANGE

		// ACT
		final String response = getREST().getForObject(getURL() + "/monitoring", String.class);

		// ASSERT
		Assert.assertNotNull(response);
		Assert.assertTrue(response.contains("<table"));
	}

}
