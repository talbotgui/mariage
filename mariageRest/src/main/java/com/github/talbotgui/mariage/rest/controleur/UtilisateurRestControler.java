package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.github.talbotgui.mariage.metier.service.SecuriteService;
import com.github.talbotgui.mariage.rest.controleur.dto.UtilisateurDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;
import com.github.talbotgui.mariage.rest.security.SecurityFilter;

@RestController
public class UtilisateurRestControler {

	@Autowired
	private SecuriteService securiteService;

	@RequestMapping(value = SecurityFilter.LOGIN_REST, method = POST)
	public void connexion(//
			@RequestParam(value = "login") final String login, //
			@RequestParam(value = "mdp") final String mdp, //
			final HttpServletRequest request, //
			final HttpServletResponse response) {
		final Role role = this.securiteService.verifieUtilisateur(login, mdp);
		request.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_LOGIN, login);
		request.getSession().setAttribute(SecurityFilter.SESSION_KEY_USER_ROLE, role.toString());
		this.resetCookieIdMariage(request, response);
	}

	@RequestMapping(value = SecurityFilter.LOGOUT_REST, method = GET)
	public void deconnexion(final HttpServletRequest request, final HttpServletResponse response) {
		request.getSession().removeAttribute("USER_LOGIN");
		request.getSession().invalidate();
		this.resetCookieIdMariage(request, response);
	}

	@RequestMapping(value = "/utilisateur/{login}/deverrouille", method = GET)
	public void deverrouilleUtilisateur(@PathVariable(value = "login") final String login) {
		this.securiteService.deverrouilleUtilisateur(login);
	}

	private Utilisateur.Role getRoleFromString(final String role) {
		Role roleEnum = null;
		try {
			if (role != null && role.length() > 0) {
				roleEnum = Role.valueOf(role);
			}
		} catch (final IllegalArgumentException e) {
			throw new RestException(RestException.ERREUR_VALEUR_PARAMETRE, e,
					new Object[] { "role", Role.values(), role });
		}
		return roleEnum;
	}

	@RequestMapping(value = "/utilisateur", method = GET)
	public Collection<UtilisateurDTO> listeUtilisateur() {
		return DTOUtils.creerDtos(this.securiteService.listeUtilisateurs(), UtilisateurDTO.class);
	}

	/**
	 * Suppression du cookie idMariage.
	 */
	private void resetCookieIdMariage(final HttpServletRequest request, final HttpServletResponse response) {
		if (request.getCookies() != null) {
			for (final Cookie c : request.getCookies()) {
				if ("idMariage".equals(c.getName())) {
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}
	}

	@RequestMapping(value = "/utilisateur", method = POST)
	public void sauvegardeUtilisateur(//
			@RequestParam(value = "login") final String login, //
			@RequestParam(value = "mdp", required = false) final String mdp, //
			@RequestParam(value = "role", required = false) final String role) {
		// Validation coté WEB car elle est nécessaire à cause d'un problème WEB
		// (au delete avec un . dans le parametre)
		if (login.contains(".")) {
			throw new RestException(RestException.ERREUR_VALEUR_PARAMETRE,
					new String[] { "login", "a-zA-Z0-9", "avec des caractères speciaux" });
		}
		this.securiteService.sauvegardeUtilisateur(login, mdp, this.getRoleFromString(role));
	}

	@RequestMapping(value = "/utilisateur/{login}", method = DELETE)
	public void supprimeUtilisateur(@PathVariable(value = "login") final String login) {
		this.securiteService.supprimeUtilisateur(login);
	}
}
