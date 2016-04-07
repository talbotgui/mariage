package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.AbstractDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.EtapeDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;

@RestController
public class EtapeRestControler {

	private static final Logger LOG = LoggerFactory.getLogger(EtapeRestControler.class);

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/etape", method = GET)
	public Collection<EtapeDTO> listeEtapeParIdMariage(//
			@PathVariable("idMariage") Long idMariage) {
		return AbstractDTO.creerDto(this.mariageService.listeEtapesParIdMariage(idMariage), EtapeDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/etape", method = POST)
	public Long sauvegardeEtape(//
			@RequestParam(value = "celebrant") String celebrant, //
			@RequestParam(value = "dateHeure") String dateHeure, //
			@RequestParam(required = false, value = "id") Long id, //
			@RequestParam(value = "lieu") String lieu, //
			@RequestParam(value = "nom") String nom, //
			@RequestParam(value = "type") String type, //
			@PathVariable(value = "idMariage") Long idMariage) {

		// Transformation de la date
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(AbstractDTO.FORMAT_DATE);
			date = sdf.parse(dateHeure);
		} catch (ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new RestException(RestException.ERREUR_FORMAT_DATE, e,
					new String[] { AbstractDTO.FORMAT_DATE, dateHeure });
		}

		// Gestion des types
		Etape etape = null;
		if (EtapeCeremonie.class.getSimpleName().equals(type)) {
			etape = new EtapeCeremonie(nom, date, lieu, celebrant);
		} else if (EtapeRepas.class.getSimpleName().equals(type)) {
			etape = new EtapeRepas(nom, date, lieu);
		} else {
			List<String> listeTypes = Arrays.asList(EtapeCeremonie.class.getSimpleName(),
					EtapeRepas.class.getSimpleName());
			throw new RestException(RestException.ERREUR_VALEUR_PARAMETRE, new Object[] { "type", listeTypes, type });
		}

		// Sauvegarde
		return this.mariageService.sauvegarde(idMariage, etape);
	}

	@RequestMapping(value = "/mariage/{idMariage}/etape/{idEtape}", method = DELETE)
	public void supprimeEtape(//
			@PathVariable(value = "idEtape") Long idEtape, //
			@PathVariable(value = "idMariage") Long idMariage) {
		this.mariageService.suprimeEtape(idMariage, idEtape);
	}
}
