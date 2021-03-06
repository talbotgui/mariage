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

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.EtapeDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;

@RestController
public class EtapeRestControler {

	private static final Logger LOG = LoggerFactory.getLogger(EtapeRestControler.class);

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/etape", method = GET)
	public Collection<EtapeDTO> listerEtapeParIdMariage(@PathVariable("idMariage") final Long idMariage) {
		return DTOUtils.creerDtos(this.mariageService.listerEtapesParIdMariage(idMariage), EtapeDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/etape", method = POST)
	public Long sauvegarderEtape(//
			@RequestParam(required = false, value = "celebrant") final String celebrant, //
			@RequestParam(value = "dateHeure") final String dateHeure, //
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(value = "lieu") final String lieu, //
			@RequestParam(value = "nom") final String nom, //
			@RequestParam(required = false, value = "numOrdre") final String numOrdre, //
			@RequestParam(value = "type") final String type, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		// Transformation de la date
		Date date = null;
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(DTOUtils.FORMAT_DATE_TIME);
			date = sdf.parse(dateHeure);
		} catch (final ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new RestException(RestException.ERREUR_FORMAT_DATE, e,
					new String[] { DTOUtils.FORMAT_DATE_TIME, dateHeure });
		}

		// Gestion des types
		Etape etape = null;
		if (EtapeCeremonie.class.getSimpleName().equals(type)) {
			etape = new EtapeCeremonie(id, nom, date, lieu, celebrant);
		} else if (EtapeRepas.class.getSimpleName().equals(type)) {
			etape = new EtapeRepas(id, nom, date, lieu);
		} else {
			final List<String> listeTypes = Arrays.asList(EtapeCeremonie.class.getSimpleName(),
					EtapeRepas.class.getSimpleName());
			throw new RestException(RestException.ERREUR_VALEUR_PARAMETRE, //
					new Object[] { "type", listeTypes, type });
		}

		// Gestion de l'ordre
		if (numOrdre != null) {
			if (numOrdre.matches("\\d+")) {
				etape.setNumOrdre(Integer.decode(numOrdre));
			} else {
				throw new RestException(RestException.ERREUR_FORMAT_NOMBRE, new Object[] { numOrdre });
			}
		}

		// Sauvegarde
		return this.mariageService.sauvegarder(idMariage, etape);
	}

	@RequestMapping(value = "/mariage/{idMariage}/etape/{idEtape}", method = DELETE)
	public void supprimerEtape(//
			@PathVariable(value = "idEtape") final Long idEtape, //
			@PathVariable(value = "idMariage") final Long idMariage) {
		this.mariageService.supprimerEtape(idMariage, idEtape);
	}
}
