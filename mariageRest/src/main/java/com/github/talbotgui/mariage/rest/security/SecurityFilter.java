package com.github.talbotgui.mariage.rest.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;

@Component
@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

	protected static final String LOGIN_PAGE = "/login.html";
	public static final String LOGIN_REST = "/dologin";
	public static final String LOGOUT_REST = "/dologout";
	public static final String SESSION_KEY_USER_LOGIN = "USER_LOGIN";
	public static final String SESSION_KEY_USER_ROLE = "USER_ROLE";

	/**
	 * Pour ne pas ajouter deux fois le même header (pb dans les navigateurs)
	 *
	 * @param response
	 *            La réponse HTTP
	 * @param name
	 *            Nom de l'entete à initialiser si ce n'est pas déjà fait
	 * @param value
	 *            Valeur à utiliser
	 */
	private void addResponseHeader(final HttpServletResponse response, final String name, final String value) {
		if (response.getHeader(name) == null) {
			response.addHeader(name, value);
		}
	}

	private void addResponseHeaders(final HttpServletRequest request, final HttpServletResponse response) {
		// N'impose pas le HTTPs en local
		final boolean httpOnly = request.getHeader("X-FORWARDED-FOR") == null
				&& request.getRemoteAddr().equals("127.0.0.1")
				&& request.getRequestURL().toString().contains("://localhost:9090");

		this.addResponseHeader(response, "X-XSS-Protection", "1; mode=block;");
		this.addResponseHeader(response, "X-Frame-Options", "DENY");
		this.addResponseHeader(response, "X-Content-Type-Options", "nosniff");
		if (httpOnly) {
			this.addResponseHeader(response, "Content-Security-Policy", "child-src 'none'; object-src 'none'");
		} else {
			this.addResponseHeader(response, "Content-Security-Policy", "child-src 'none'; object-src 'none'");
			this.addResponseHeader(response, "Strict-Transport-Security",
					"max-age=31536000; includeSubDomains; preload");
		}
	}

	private void checkUserIsLoggedIn(final FilterChain chain, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException, ServletException {
		final boolean pageProtegee = this.isPageProtegee(request);
		LOG.debug("{} => {}", request.getRequestURI(), pageProtegee);

		// Page sécurisée et login valide
		if (pageProtegee && request.getSession().getAttribute(SESSION_KEY_USER_LOGIN) != null) {
			chain.doFilter(request, response);
		}

		// Page WEB sécurisée
		else if (pageProtegee && request.getRequestURI().endsWith(".html")) {
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
		}

		// API sécurisée
		else if (pageProtegee && !request.getRequestURI().endsWith(".html")) {
			this.return404(response);
		}

		// Sinon
		else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// Rien à faire
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		final boolean reponseTraite = this.filterRestByUserRole(request, response);

		if (!reponseTraite) {
			this.addResponseHeaders(request, response);
			this.checkUserIsLoggedIn(chain, request, response);
		}
	}

	/**
	 * Renvoi un cangeMdpode 404 pour les appels REST "/utilisateur" pour les
	 * utilisateurs du role non ADMIN.
	 *
	 * @param request
	 *            Requete HTTP
	 * @param response
	 *            Response HTTP
	 * @return TRUE si la response est traité et que rien ne doit plus être fait
	 */
	private boolean filterRestByUserRole(final HttpServletRequest request, final HttpServletResponse response) {
		final String role = (String) request.getSession().getAttribute(SESSION_KEY_USER_ROLE);
		final String login = (String) request.getSession().getAttribute(SESSION_KEY_USER_LOGIN);

		if (!Role.ADMIN.toString().equals(role)
				&& !request.getRequestURI().contains(request.getContextPath() + "/utilisateur/moi")
				&& !request.getRequestURI().contains(request.getContextPath() + "/utilisateur/" + login + "/changeMdp")
				&& (request.getRequestURI().contains(request.getContextPath() + "/utilisateur")
						|| request.getRequestURI().contains(request.getContextPath() + "/autorisation"))) {
			this.return404(response);
			return true;
		}

		return false;
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// Rien à faire
	}

	private boolean isPageProtegee(final HttpServletRequest request) {
		final String uriAcomparer = request.getRequestURI().replaceFirst(request.getContextPath(), "");
		return !LOGIN_PAGE.equals(uriAcomparer) && !LOGIN_REST.equals(uriAcomparer)
				&& !uriAcomparer.startsWith("/ressources/") && !uriAcomparer.startsWith("/v2/")
				&& !"/".equals(uriAcomparer) && !uriAcomparer.startsWith("/swagger-ui.html");
	}

	private void return404(final HttpServletResponse response) {
		response.resetBuffer();
		response.setStatus(HttpStatus.NOT_FOUND.value());
	}

}
