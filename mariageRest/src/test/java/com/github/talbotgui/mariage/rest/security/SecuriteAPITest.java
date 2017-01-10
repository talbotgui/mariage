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

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecuriteAPITest {

	private void assertPage404(final MockHttpServletResponse res) {
		Assert.assertEquals(404, res.getStatus());
	}

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
	public void test01AccesApi01SansSession() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/mariage");
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertPage404(res);
	}

	@Test
	public void test01AccesApi02AvecSessionUtilisateur() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/mariage");
		req.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_LOGIN, SecurityFilter.SESSION_KEY_USER_LOGIN);
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertStatutOkEtHeaders(res);
	}

	@Test
	public void test02AccesApiAdmin01SansSession() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/utilisateur");
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertPage404(res);
	}

	@Test
	public void test02AccesApiAdmin02AvecSessionUtilisateur() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/utilisateur");
		req.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_LOGIN, SecurityFilter.SESSION_KEY_USER_LOGIN);
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertPage404(res);
	}

	@Test
	public void test02AccesApiAdmin03AvecSessionAdmin() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/utilisateur");
		req.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_LOGIN, SecurityFilter.SESSION_KEY_USER_LOGIN);
		req.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_ROLE, Role.ADMIN.name());
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertStatutOkEtHeaders(res);
	}

	@Test
	public void test02AccesApiAdminMoi04UtilisateurMoiAvecSessionUtilisateur() throws IOException, ServletException {
		// Arrange
		final SecurityFilter sf = new SecurityFilter();

		// Act
		final MockHttpServletRequest req = new MockHttpServletRequest("GET", "/utilisateur/moi");
		req.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_LOGIN, SecurityFilter.SESSION_KEY_USER_LOGIN);
		final MockHttpServletResponse res = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		sf.doFilter(req, res, chain);

		// Assert
		this.assertStatutOkEtHeaders(res);
	}
}
