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

import com.github.talbotgui.mariage.metier.service.SecuriteService;
import com.github.talbotgui.mariage.rest.controleur.dto.AbstractDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.UtilisateurDTO;

@RestController
public class UtilisateurRestControler {

	@Autowired
	private SecuriteService securiteService;

	@RequestMapping(value = "/utilisateur", method = GET)
	public Collection<UtilisateurDTO> listeUtilisateur() {
		return AbstractDTO.creerDto(this.securiteService.listeUtilisateurs(), UtilisateurDTO.class);
	}

	@RequestMapping(value = "/utilisateur", method = POST)
	public void sauvegardeUtilisateur(//
			@RequestParam(value = "login") final String login, //
			@RequestParam(value = "mdp") final String mdp) {
		this.securiteService.creeUtilisateur(login, mdp);
	}

	@RequestMapping(value = "/utilisateur/{login}", method = DELETE)
	public void supprimeUtilisateur(@PathVariable(value = "login") final String login) {
		this.securiteService.supprimeUtilisateur(login);
	}
}
