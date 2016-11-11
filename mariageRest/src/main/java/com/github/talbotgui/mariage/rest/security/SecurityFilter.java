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

	private void addResponseHeaders(final HttpServletRequest request, final HttpServletResponse response) {
		// N'impose pas le HTTPs en local
		final boolean httpOnly = request.getHeader("X-FORWARDED-FOR") == null
				&& request.getRemoteAddr().equals("127.0.0.1")
				&& request.getRequestURL().toString().contains("://localhost:9090");

		response.addHeader("X-XSS-Protection", "1; mode=block;");
		response.addHeader("X-Frame-Options", "DENY");
		response.addHeader("X-Content-Type-Options", "nosniff");
		if (httpOnly) {
			response.addHeader("Content-Security-Policy", "child-src 'none'; object-src 'none'");
		} else {
			response.addHeader("Content-Security-Policy", "child-src 'none'; object-src 'none'");// default-src
																									// https:;
			response.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
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

		// Page sécurisée
		else if (pageProtegee) {
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
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

		boolean reponseTraite = this.filterRestByUserRole(request, response);

		if (!reponseTraite) {
			reponseTraite = this.filterMenuItemsByUserRole(request, response);
		}
		if (!reponseTraite) {
			this.addResponseHeaders(request, response);
			this.checkUserIsLoggedIn(chain, request, response);
		}
	}

	/**
	 * Renvoi la page part_menu_{LE_ROLE_UTILISATEUR}.html
	 *
	 * @param request
	 *            Requete HTTP
	 * @param response
	 *            Response HTTP
	 * @return TRUE si la response est traité et que rien ne doit plus être fait
	 * @throws IOException
	 */
	private boolean filterMenuItemsByUserRole(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		if (!request.getRequestURI().contains("part_menu.html")) {
			return false;
		}
		final String role = (String) request.getSession().getAttribute(SESSION_KEY_USER_ROLE);
		response.sendRedirect(request.getRequestURL().toString().replaceAll("/part_menu", "/part_menu_" + role));
		return true;
	}

	/**
	 * Renvoi un code 404 pour les appels REST "/utilisateur" pour les
	 * utilisateurs du role non ADMIN
	 *
	 * @param request
	 *            Requete HTTP
	 * @param response
	 *            Response HTTP
	 * @return TRUE si la response est traité et que rien ne doit plus être fait
	 */
	private boolean filterRestByUserRole(final HttpServletRequest request, final HttpServletResponse response) {
		final String role = (String) request.getSession().getAttribute(SESSION_KEY_USER_ROLE);

		if (!Role.ADMIN.toString().equals(role)
				&& request.getRequestURI().contains(request.getContextPath() + "/utilisateur")) {
			response.resetBuffer();
			response.setStatus(HttpStatus.NOT_FOUND.value());
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
				&& !uriAcomparer.startsWith("/ressources/");
	}

}
