package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.MariageDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;
import com.github.talbotgui.mariage.rest.security.SecurityFilter;

@RestController
public class MariageRestControler {

	private static final Logger LOG = LoggerFactory.getLogger(MariageRestControler.class);

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}", method = GET)
	public MariageDTO chargerMariage(@PathVariable("idMariage") final Long idMariage) {
		final Mariage mariage = this.mariageService.chargerMariageParId(idMariage);
		return new MariageDTO(mariage);
	}

	@RequestMapping(value = "/mariage", method = GET)
	public Collection<MariageDTO> listerTousMariages(final HttpServletRequest request) {
		final String login = (String) request.getSession().getAttribute(SecurityFilter.SESSION_KEY_USER_LOGIN);
		final String roleUtilisateur = (String) request.getSession().getAttribute(SecurityFilter.SESSION_KEY_USER_ROLE);

		final Collection<Mariage> mariages;
		if (Role.ADMIN.name().equals(roleUtilisateur)) {
			mariages = this.mariageService.listerTousMariages();
		} else {
			mariages = this.mariageService.listerMariagesAutorises(login);
		}

		return DTOUtils.creerDtos(mariages, MariageDTO.class);
	}

	@RequestMapping(value = "/mariage", method = POST)
	public Long sauvegarderMariage(//
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(value = "dateCelebration") final String dateCelebration, //
			@RequestParam(value = "marie1") final String marie1, //
			@RequestParam(value = "marie2") final String marie2) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(DTOUtils.FORMAT_DATE);
			return this.mariageService.sauvegarder(new Mariage(id, sdf.parse(dateCelebration), marie1, marie2));
		} catch (final ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new RestException(RestException.ERREUR_FORMAT_DATE, e,
					new String[] { DTOUtils.FORMAT_DATE, dateCelebration });
		}
	}

	@RequestMapping(value = "/mariage/{idMariage}", method = DELETE)
	public void supprimerMariage(@PathVariable("idMariage") final Long idMariage) {
		this.mariageService.supprimerMariage(idMariage);
	}

}
