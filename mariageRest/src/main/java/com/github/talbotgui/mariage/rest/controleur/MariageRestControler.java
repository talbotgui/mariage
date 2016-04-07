package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.exception.BusinessException;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.AbstractDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.InviteDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.MariageDTO;

@RestController
public class MariageRestControler {

	private static final Logger LOG = LoggerFactory.getLogger(MariageRestControler.class);

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}", method = GET)
	public MariageDTO chargeMariage(@PathVariable("idMariage") Long idMariage) {
		Mariage mariage = this.mariageService.chargeMariageParId(idMariage);
		return new MariageDTO(mariage);
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = GET)
	public Collection<InviteDTO> listeInvitesParIdMariage(@PathVariable("idMariage") Long idMariage) {
		return AbstractDTO.creerDto(this.mariageService.listeInvitesParIdMariage(idMariage), InviteDTO.class);
	}

	@RequestMapping(value = "/mariage", method = GET)
	public Collection<MariageDTO> listeTousMariages() {
		Collection<Mariage> mariages = this.mariageService.listeTousMariages();

		Collection<MariageDTO> dtos = new ArrayList<>();
		for (Mariage m : mariages) {
			dtos.add(new MariageDTO(m));
		}
		return dtos;
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = POST)
	public Long sauvegardeInvite(@RequestParam(required = false, value = "id") Long id,
			@RequestParam(value = "nom") String nom, @RequestParam(value = "groupe") String groupe,
			@PathVariable(value = "idMariage") Long idMariage) {
		return this.mariageService.sauvegarde(idMariage, new Invite(id, groupe, nom));
	}

	@RequestMapping(value = "/mariage", method = POST)
	public Long sauvegardeMariage(@RequestParam(required = false, value = "id") Long id,
			@RequestParam(value = "dateCelebration") String dateCelebration,
			@RequestParam(value = "marie1") String marie1, @RequestParam(value = "marie2") String marie2) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(AbstractDTO.FORMAT_DATE);
			return this.mariageService.sauvegarde(new Mariage(id, sdf.parse(dateCelebration), marie1, marie2));
		} catch (ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new BusinessException(BusinessException.ERREUR_FORMAT_DATE, e,
					new String[] { AbstractDTO.FORMAT_DATE, dateCelebration });
		}
	}

	@RequestMapping(value = "/mariage/{idMariage}/invite", method = DELETE)
	public void supprimeInvite(@RequestParam(value = "id") Long id, @PathVariable(value = "idMariage") Long idMariage) {
		this.mariageService.suprimeInvite(idMariage, id);
	}
}
