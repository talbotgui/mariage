package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.FoyerDTO;

@RestController
public class FoyerRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/foyer", method = GET)
	public Collection<FoyerDTO> listeFoyersParIdMariage(@PathVariable("idMariage") final Long idMariage) {
		return DTOUtils.creerDtos(this.mariageService.listeFoyersParIdMariage(idMariage), FoyerDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/foyer/{idFoyer}", method = POST)
	public void modifieInvitationFoyer(//
			@RequestParam(value = "idEtape") final Long idEtape, //
			@RequestParam(value = "estInvite") final Boolean estInvite, //
			@PathVariable(value = "idFoyer") final Long idFoyer, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		this.mariageService.modifieFoyerEtapeInvitation(idMariage, idEtape, idFoyer, estInvite);
	}

	@RequestMapping(value = "/mariage/{idMariage}/foyer", method = POST)
	public Long sauvegardeFoyer(//
			@RequestParam(required = false, value = "adresse") final String adresse, //
			@RequestParam(required = false, value = "email") final String email, //
			@RequestParam(required = false, value = "foyer") final String nomFoyer, //
			@RequestParam(required = false, value = "groupe") final String groupe, //
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(required = false, value = "telephone") final String telephone, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		Foyer foyer;
		if (id == null) {
			foyer = new Foyer(groupe, nomFoyer, adresse, email, telephone);
		} else {
			foyer = this.mariageService.chargeFoyerParId(id);
			if (nomFoyer != null) {
				foyer.setNom(nomFoyer);
			}
			if (adresse != null) {
				foyer.setAdresse(adresse);
			}
			if (email != null) {
				foyer.setEmail(email);
			}
			if (groupe != null) {
				foyer.setGroupe(groupe);
			}
			if (telephone != null) {
				foyer.setTelephone(telephone);
			}
		}
		return this.mariageService.sauvegarde(idMariage, foyer);
	}

}
