package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.service.SecuriteService;
import com.github.talbotgui.mariage.rest.controleur.dto.AutorisationDTO;

@RestController
public class AutorisationRestControler {

	@Autowired
	private SecuriteService securiteService;

	@RequestMapping(value = "/autorisation", method = GET)
	public Collection<AutorisationDTO> listerAutorisations() {
		return DTOUtils.creerDtos(this.securiteService.listerAutorisations(), AutorisationDTO.class);
	}

	@RequestMapping(value = "/autorisation", method = POST)
	public void sauvegarderAutorisation(//
			@RequestParam(value = "login") final String login, //
			@RequestParam(value = "idMariage") final Long idMariage) {
		this.securiteService.ajouterAutorisation(login, idMariage);
	}

	@RequestMapping(value = "/autorisation/{idAutorisation}", method = DELETE)
	public void supprimerAutorisation(@PathVariable(value = "idAutorisation") final Long idAutorisation) {
		this.securiteService.supprimerAutorisation(idAutorisation);
	}
}
