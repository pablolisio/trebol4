package com.proit;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.proit.vista.login.LoginPage;
import com.proit.wicket.FacturarOnLineAuthenticatedWebApplication;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new FacturarOnLineAuthenticatedWebApplication());
	}

	@Test
	public void homepageRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(LoginPage.class);

		//assert rendered page class
		tester.assertRenderedPage(LoginPage.class);
	}
}
