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

import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.AbstractDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;

@RestController
public class InviteRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = GET)
	public Collection<InviteDTO> listeInvitesParIdMariage(@PathVariable("idMariage") Long idMariage) {
		return AbstractDTO.creerDto(this.mariageService.listeInvitesParIdMariage(idMariage), InviteDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = POST)
	public Long sauvegardeInvite(//
			@RequestParam(required = false, value = "id") Long id, //
			@RequestParam(value = "nom") String nom, //
			@RequestParam(value = "groupe") String groupe, //
			@PathVariable(value = "idMariage") Long idMariage) {
		return this.mariageService.sauvegarde(idMariage, new Invite(id, groupe, nom));
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite/{idInvite}", method = DELETE)
	public void supprimeInvite(//
			@PathVariable(value = "idInvite") Long idInvite, //
			@PathVariable(value = "idMariage") Long idMariage) {
		this.mariageService.suprimeInvite(idMariage, idInvite);
	}
}
