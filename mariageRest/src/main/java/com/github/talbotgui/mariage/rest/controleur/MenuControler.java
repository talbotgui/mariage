package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.rest.security.SecurityFilter;

@RestController
public class MenuControler {

	private static final Logger LOG = LoggerFactory.getLogger(MenuControler.class);

	@RequestMapping(value = "/part_menu.html", method = GET, produces = "text/html;charset=UTF-8")
	public String getMenu(final HttpServletRequest request, final HttpServletResponse response) {
		final String roleUtilisateur = (String) request.getSession().getAttribute(SecurityFilter.SESSION_KEY_USER_ROLE);
		final String nomPageMenu = "part_menu_" + roleUtilisateur + ".html";

		try {
			final Path path = Paths.get(this.getClass().getClassLoader().getResource(nomPageMenu).toURI());
			return new String(Files.readAllBytes(path), "utf8");
		} catch (final IOException | URISyntaxException e) {
			LOG.error("Erreur de chargement du menu", e);
			return "";
		}
	}
}
