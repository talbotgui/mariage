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
import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;

@RestController
public class InviteRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/statistiques", method = GET)
	public StatistiquesMariage calculStatistiques(@PathVariable("idMariage") final Long idMariage) {
		return this.mariageService.calculStatistiques(idMariage);
	}

	private Age getAgeFromString(final String age) {
		Age ageEnum = null;
		try {
			if (age != null && age.length() > 0) {
				ageEnum = Age.valueOf(age);
			}
		} catch (final IllegalArgumentException e) {
			throw new RestException(RestException.ERREUR_VALEUR_PARAMETRE, e,
					new Object[] { "age", Age.values(), age });
		}
		return ageEnum;
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = GET)
	public Collection<InviteDTO> listeInvitesParIdMariage(@PathVariable("idMariage") final Long idMariage) {
		return DTOUtils.creerDtos(this.mariageService.listeInvitesParIdMariage(idMariage), InviteDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = POST)
	public Long sauvegardeInvite(//
			@RequestParam(required = false, value = "adresse") final String adresse, //
			@RequestParam(required = false, value = "email") final String email, //
			@RequestParam(required = false, value = "age") final String age, //
			@RequestParam(required = false, value = "foyer") final String foyer, //
			@RequestParam(required = false, value = "groupe") final String groupe, //
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(required = false, value = "nom") final String nom, //
			@RequestParam(required = false, value = "prenom") final String prenom, //
			@RequestParam(required = false, value = "telephone") final String telephone, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		Invite invite;
		if (id == null) {
			invite = new Invite(id, nom, prenom, this.getAgeFromString(age));
			invite.setFoyer(new Foyer(null, groupe, foyer, adresse, email, telephone));
			;
		} else {
			invite = this.mariageService.chargeInviteParId(id);
			if (nom != null) {
				invite.setNom(nom);
			}
			if (prenom != null) {
				invite.setPrenom(prenom);
			}
			if (age != null) {
				invite.setAge(this.getAgeFromString(age));
			}
			if (adresse != null) {
				invite.getFoyer().setAdresse(adresse);
			}
			if (email != null) {
				invite.getFoyer().setEmail(email);
			}
			if (foyer != null) {
				invite.getFoyer().setNom(foyer);
			}
			if (groupe != null) {
				invite.getFoyer().setGroupe(groupe);
			}
			if (telephone != null) {
				invite.getFoyer().setTelephone(telephone);
			}
		}
		return this.mariageService.sauvegardeInviteEtFoyer(idMariage, invite);
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite/{idInvite}", method = DELETE)
	public void supprimeInvite(//
			@PathVariable(value = "idInvite") final Long idInvite, //
			@PathVariable(value = "idMariage") final Long idMariage) {
		this.mariageService.supprimeInvite(idMariage, idInvite);
	}
}
