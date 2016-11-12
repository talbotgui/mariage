package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.rest.security.SecurityFilter;

@RestController
public class MenuControler {

	@RequestMapping(value = "/part_menu.html", method = GET, produces = "text/html;charset=UTF-8")
	public String getMenu(final HttpServletRequest request, final HttpServletResponse response) {
		final String roleUtilisateur = (String) request.getSession().getAttribute(SecurityFilter.SESSION_KEY_USER_ROLE);
		final String nomPageMenu = "part_menu_" + roleUtilisateur + ".html";

		// L'accès par Files/Paths ne fonctionne pas quand le fichier est dans
		// un WAR
		final InputStream in = this.getClass().getClassLoader().getResourceAsStream(nomPageMenu);
		try (Scanner scanner = new Scanner(in, "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
