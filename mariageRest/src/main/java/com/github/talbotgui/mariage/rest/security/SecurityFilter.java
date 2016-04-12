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

import org.springframework.stereotype.Component;

@Component
@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {

	private static final String LOGIN_PAGE = "/login.html";
	public static final String LOGIN_REST = "/dologin";
	public static final String LOGOUT_REST = "/dologout";

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		// Securité
		if (isPageProtegee(request.getRequestURI()) && request.getSession().getAttribute("USER_LOGIN") == null) {
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
		}

		// si utilisateur connecté
		else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	private boolean isPageProtegee(final String uri) {
		return !LOGIN_PAGE.equals(uri) && !LOGIN_REST.equals(uri) && !uri.startsWith("/ressources");
	}

}
