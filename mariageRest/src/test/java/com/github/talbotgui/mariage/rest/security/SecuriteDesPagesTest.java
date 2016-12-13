package com.github.talbotgui.mariage.rest.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecuriteDesPagesTest {

	private void assertRedirectionPageLogin(final MockHttpServletResponse res) {
		Assert.assertEquals(302, res.getStatus());
		Assert.assertEquals("/login.html", res.getHeaderValue("Location"));
	}

	private void assertStatutOkEtHeaders(final HttpServletResponse res) {
		Assert.assertEquals(200, res.getStatus());
		Assert.assertNotNull(res.getHeader("X-XSS-Protection"));
		Assert.assertNotNull(res.getHeader("X-Frame-Options"));
		Assert.assertNotNull(res.getHeader("X-Content-Type-Options"));
		Assert.assertNotNull(res.getHeader("Content-Security-Policy"));
		Assert.assertNotNull(res.getHeader("Strict-Transport-Security"));
	}

	@Test
	public void test01RessourceStatique01Redirection() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/ressources/monScript.js");
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertStatutOkEtHeaders(res);
	}

	@Test
	public void test01RessourceStatique02QuiNenEstPasUne() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/ressources.html");
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertRedirectionPageLogin(res);
	}

	@Test
	public void test01RessourceStatique03QuiNenEstPasUne() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/toto/ressources/page.html");
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertRedirectionPageLogin(res);
	}

	@Test
	public void test02PageRoot01SansSession() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/");
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertRedirectionPageLogin(res);
	}

	@Test
	public void test02PageRoot02AvecSession() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/");
		req.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_LOGIN, SecurityFilter.SESSION_KEY_USER_LOGIN);
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertStatutOkEtHeaders(res);
	}

	@Test
	public void test03LoginSansSession01LoginRest() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", SecurityFilter.LOGIN_REST);
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertStatutOkEtHeaders(res);
	}

	@Test
	public void test03LoginSansSession02LoginPage() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", SecurityFilter.LOGIN_PAGE);
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertStatutOkEtHeaders(res);
	}
}
