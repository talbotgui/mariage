package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.service.MariageService;

@RestController
public class PresenceEtapeRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/presenceEtape", method = POST)
	public void modifiePresenceEtape(//
			@RequestParam(value = "id") final Long idPresenceEtape, //
			@RequestParam(value = "presence") final Boolean presence, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		this.mariageService.modifiePresenceEtape(idMariage, idPresenceEtape, presence);
	}
}
