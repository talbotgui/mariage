package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.service.MariageService;

@RestController
public class FoyerRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/foyer/{idFoyer}", method = POST)
	public void modifieInvitationFoyer(//
			@RequestParam(value = "idEtape") final Long idEtape, //
			@RequestParam(value = "estInvite") final Boolean estInvite, //
			@PathVariable(value = "idFoyer") final Long idFoyer, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		this.mariageService.modifieFoyerEtapeInvitation(idMariage, idEtape, idFoyer, estInvite);
	}

}
