package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.AbstractDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.CourrierDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;

@RestController
public class CourrierRestControler {

	private static final Logger LOG = LoggerFactory.getLogger(CourrierRestControler.class);

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/courrier/{idCourrier}", method = POST)
	public void lieCourrierEtEtape(//
			@RequestParam(value = "idEtape") final Long idEtape, //
			@RequestParam(value = "lie") final Boolean lie, //
			@PathVariable(value = "idMariage") final Long idMariage, //
			@PathVariable(value = "idCourrier") final Long idCourrier) {
		mariageService.lieUneEtapeEtUnCourrier(idMariage, idEtape, idCourrier, lie);
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier", method = GET)
	public Collection<CourrierDTO> listeCourrierParIdMariage(@PathVariable("idMariage") final Long idMariage) {
		return AbstractDTO.creerDto(this.mariageService.listeCourriersParIdMariage(idMariage), CourrierDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier", method = POST)
	public Long sauvegardeCourrier(//
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(value = "nom") final String nom, //
			@RequestParam(value = "datePrevisionEnvoi") final String sdatePrevisionEnvoi, //
			@RequestParam(required = false, value = "dateEnvoiRealise") final String sdateEnvoiRealise, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		// Transformation des dates
		final SimpleDateFormat sdf = new SimpleDateFormat(AbstractDTO.FORMAT_DATE);
		Date datePrevisionEnvoi = null;
		Date dateEnvoiRealise = null;
		try {
			datePrevisionEnvoi = sdf.parse(sdatePrevisionEnvoi);
		} catch (final ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new RestException(RestException.ERREUR_FORMAT_DATE, e,
					new String[] { AbstractDTO.FORMAT_DATE_TIME, sdatePrevisionEnvoi });
		}
		try {
			if (StringUtils.isNotBlank(sdateEnvoiRealise)) {
				dateEnvoiRealise = sdf.parse(sdateEnvoiRealise);
			}
		} catch (final ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new RestException(RestException.ERREUR_FORMAT_DATE, e,
					new String[] { AbstractDTO.FORMAT_DATE_TIME, sdateEnvoiRealise });
		}

		// Gestion des types
		final Courrier courrier = new Courrier(id, nom, datePrevisionEnvoi, dateEnvoiRealise);

		// Sauvegarde
		return this.mariageService.sauvegarde(idMariage, courrier);
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier/{idCourrier}", method = DELETE)
	public void supprimeCourrier(//
			@PathVariable(value = "idCourrier") final Long idCourrier, //
			@PathVariable(value = "idMariage") final Long idMariage) {
		this.mariageService.suprimeCourrier(idMariage, idCourrier);
	}
}
