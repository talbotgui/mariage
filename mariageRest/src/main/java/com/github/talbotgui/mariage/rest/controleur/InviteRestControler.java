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
import com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Presence;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.PresenceDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;

@RestController
public class InviteRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/statistiques", method = GET)
	public StatistiquesMariage calculStatistiques(@PathVariable("idMariage") final Long idMariage) {
		return this.mariageService.calculStatistiques(idMariage);
	}

	@RequestMapping(value = "/mariage/{idMariage}/etape/{idEtape}/statistiquesPresence", method = GET)
	public Collection<StatistiquesPresenceMariage> calculStatistiquesPresence(
			@PathVariable("idMariage") final Long idMariage, @PathVariable("idEtape") final Long idEtape) {
		return this.mariageService.calculStatistiquesPresence(idMariage, idEtape);
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

	@RequestMapping(value = "/mariage/{idMariage}/presence", method = GET)
	public Collection<PresenceDTO> listePresenceParIdMariage(@PathVariable("idMariage") final Long idMariage) {
		return DTOUtils.creerDtos(this.mariageService.listePresencesParIdMariage(idMariage), PresenceDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/erreurs", method = GET)
	public Collection<String> rechercheErreursPourInviteSurPlusieursEtapes(
			@PathVariable(value = "idMariage") final Long idMariage) {
		return this.mariageService.rechercheErreurs(idMariage);
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = POST)
	public Long sauvegardeInvite(//
			@RequestParam(required = false, value = "age") final String age, //
			@RequestParam(required = false, value = "foyer") final String nomFoyer, //
			@RequestParam(required = false, value = "groupe") final String groupe, //
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(required = false, value = "nom") final String nom, //
			@RequestParam(required = false, value = "prenom") final String prenom, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		Invite invite;
		if (id == null) {
			invite = new Invite(null, nom, prenom, this.getAgeFromString(age));
			Foyer foyer = this.mariageService.getFoyer(idMariage, nomFoyer);
			if (foyer == null) {
				foyer = new Foyer(groupe, nomFoyer);
			} else {
				foyer.setGroupe(groupe);
			}
			invite.setFoyer(foyer);
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
			if (nomFoyer != null) {
				invite.getFoyer().setNom(nomFoyer);
			}
			if (groupe != null) {
				invite.getFoyer().setGroupe(groupe);
			}
		}
		return this.mariageService.sauvegardeInviteEtFoyer(idMariage, invite);
	}

	@RequestMapping(value = "/mariage/{idMariage}/presence", method = POST)
	public void sauvegardePresence(//
			@RequestParam(required = true, value = "idEtape") final Long idEtape, //
			@RequestParam(required = true, value = "idInvite") final Long idInvite, //
			@RequestParam(required = false, value = "commentaire") final String commentaire, //
			@RequestParam(required = false, value = "confirmee") final Boolean confirmee, //
			@RequestParam(required = false, value = "present") final Boolean present, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		Presence presence = this.mariageService.chargePresenceParEtapeEtInvite(idMariage, idEtape, idInvite);
		if (presence == null) {
			presence = new Presence(new EtapeRepas(idEtape), new Invite(idInvite));
		}

		presence.setCommentaire(commentaire);
		presence.setPresent(present);
		presence.setConfirmee(confirmee);

		this.mariageService.sauvegarde(idMariage, presence);
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite/{idInvite}", method = DELETE)
	public void supprimeInvite(//
			@PathVariable(value = "idInvite") final Long idInvite, //
			@PathVariable(value = "idMariage") final Long idMariage) {
		this.mariageService.supprimeInvite(idMariage, idInvite);
	}

	@RequestMapping(value = "/mariage/{idMariage}/presence", method = DELETE)
	public void supprimePresence(//
			@RequestParam(value = "idEtape") final Long idEtape, //
			@RequestParam(value = "idInvite") final Long idInvite, //
			@PathVariable(value = "idMariage") final Long idMariage) {
		this.mariageService.supprimePresence(idMariage, idInvite, idEtape);
	}
}
