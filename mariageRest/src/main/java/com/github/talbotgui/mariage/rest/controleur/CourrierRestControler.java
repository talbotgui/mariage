package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.CourrierDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.EtapeDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.FoyerDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.ReponseAvecChoix;
import com.github.talbotgui.mariage.rest.exception.RestException;

@RestController
public class CourrierRestControler {

	private static final Logger LOG = LoggerFactory.getLogger(CourrierRestControler.class);

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}/courrier/{idCourrier}/publipostage", method = GET)
	public void generePubliPostage(//
			@PathVariable(value = "idMariage") final Long idMariage, //
			@PathVariable(value = "idCourrier") final Long idCourrier, //
			final HttpServletResponse response) {
		final String contenu = this.mariageService.generePublipostage(idMariage, idCourrier);

		try {
			response.getOutputStream().write(contenu.getBytes("UTF8"));
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=publipostage.csv");
			response.flushBuffer();
		} catch (final IOException e) {
			throw new RestException(RestException.ERREUR_IO, e);
		}
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier/{idCourrier}", method = POST)
	public void lieCourrierEtEtape(//
			@RequestParam(value = "idEtape") final Long idEtape, //
			@RequestParam(value = "lie") final Boolean lie, //
			@PathVariable(value = "idMariage") final Long idMariage, //
			@PathVariable(value = "idCourrier") final Long idCourrier) {
		this.mariageService.lieUneEtapeEtUnCourrier(idMariage, idEtape, idCourrier, lie);
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier", method = GET)
	public ReponseAvecChoix listeCourrierParIdMariage(@PathVariable("idMariage") final Long idMariage)
			throws NoSuchMethodException, SecurityException, ReflectiveOperationException, IllegalArgumentException {

		final Collection<Courrier> courriers = this.mariageService.listeCourriersParIdMariage(idMariage);
		final Collection<Etape> etapes = this.mariageService.listeEtapesParIdMariage(idMariage);
		final Method method = Courrier.class.getMethod("getEtapes");

		return new ReponseAvecChoix(etapes, EtapeDTO.class, courriers, CourrierDTO.class, method);
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier/{idCourrier}/foyers", method = GET)
	public Collection<FoyerDTO> listeFoyersParCourrier(//
			@PathVariable(value = "idMariage") final Long idMariage, //
			@PathVariable(value = "idCourrier") final Long idCourrier) {
		final Collection<Foyer> foyers = this.mariageService.listeFoyersParIdCourrier(idMariage, idCourrier);
		return DTOUtils.creerDtos(foyers, FoyerDTO.class);
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier", method = POST)
	public Long sauvegardeCourrier(//
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(value = "nom") final String nom, //
			@RequestParam(value = "datePrevisionEnvoi") final String sdatePrevisionEnvoi, //
			@PathVariable(value = "idMariage") final Long idMariage) {

		// Transformation des dates
		final SimpleDateFormat sdf = new SimpleDateFormat(DTOUtils.FORMAT_DATE);
		Date datePrevisionEnvoi = null;
		try {
			datePrevisionEnvoi = sdf.parse(sdatePrevisionEnvoi);
		} catch (final ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new RestException(RestException.ERREUR_FORMAT_DATE, e,
					new String[] { DTOUtils.FORMAT_DATE_TIME, sdatePrevisionEnvoi });
		}

		// Gestion des types
		final Courrier courrier = new Courrier(id, nom, datePrevisionEnvoi);

		// Sauvegarde
		return this.mariageService.sauvegarde(idMariage, courrier);
	}

	@RequestMapping(value = "/mariage/{idMariage}/courrier/{idCourrier}", method = DELETE)
	public void supprimeCourrier(//
			@PathVariable(value = "idCourrier") final Long idCourrier, //
			@PathVariable(value = "idMariage") final Long idMariage) {
		this.mariageService.supprimeCourrier(idMariage, idCourrier);
	}
}
