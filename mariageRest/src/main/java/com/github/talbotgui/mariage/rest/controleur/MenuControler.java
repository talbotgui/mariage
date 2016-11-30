package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.rest.security.SecurityFilter;

@RestController
public class MenuControler {

	@RequestMapping(value = "/part_menu.html", method = GET, produces = "text/html;charset=UTF-8")
	public String getMenu(final HttpServletRequest request, final HttpServletResponse response) {
		final String roleUtilisateur = (String) request.getSession().getAttribute(SecurityFilter.SESSION_KEY_USER_ROLE);
		final String mariageSelectionne = this.isCookieIdMariagePresent(request) ? "mariageSelectionne" : "sansMariage";
		final String nomPageMenu = "part_menu_" + roleUtilisateur + "-" + mariageSelectionne + ".html";

		// L'accès par Files/Paths ne fonctionne pas quand le fichier est dans
		// un WAR. Donc getResourceAsStream et scanner
		final InputStream in = this.getClass().getClassLoader().getResourceAsStream(nomPageMenu);

		// Si pas de fichier ou role invalide, return 404
		if (in == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}

		// Lecture du fichier et renvoi (sans fuite mémoire avec le try)
		try (Scanner scanner = new Scanner(in, "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}

	}

	private boolean isCookieIdMariagePresent(final HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (final Cookie c : request.getCookies()) {
				if ("idMariage".equals(c.getName())) {
					return true;
				}
			}
		}
		return false;
	}
}
