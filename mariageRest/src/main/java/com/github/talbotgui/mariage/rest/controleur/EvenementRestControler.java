package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Evenement;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.EvenementDTO;

@RestController
public class EvenementRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/evenement", method = GET)
	public Collection<EvenementDTO> listerEvenementParIdMariage(@PathVariable("idMariage") final Long idMariage) {
		return DTOUtils.creerDtos(this.mariageService.listerEvenementsParIdMariage(idMariage), EvenementDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/evenement", method = POST)
	public Long sauvegarderEvenement(//
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(value = "titre") final String titre, //
			@RequestParam(value = "debut") final Date debut, //
			@RequestParam(value = "fin") final Date fin, //
			@RequestParam(value = "participants") final List<String> participants, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		final Collection<Utilisateur> listeParticipants = new ArrayList<>();
		if (participants != null) {
			for (final String participant : participants) {
				listeParticipants.add(new Utilisateur(participant));
			}
		}

		// Sauvegarde
		return this.mariageService.sauvegarder(idMariage, new Evenement(id, titre, debut, fin, listeParticipants));
	}

	@RequestMapping(value = "/mariage/{idMariage}/evenement/{idEvenement}", method = DELETE)
	public void supprimerEvenement(//
			@PathVariable(value = "idEvenement") final Long idEvenement, //
			@PathVariable(value = "idMariage") final Long idMariage) {
		this.mariageService.supprimerEvenement(idMariage, idEvenement);
	}
}
