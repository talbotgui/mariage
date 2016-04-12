package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.rest.controleur.dto.AbstractDTO;
import com.github.talbotgui.mariage.rest.controleur.dto.MariageDTO;
import com.github.talbotgui.mariage.rest.exception.RestException;

@RestController
public class MariageRestControler {

	private static final Logger LOG = LoggerFactory.getLogger(MariageRestControler.class);

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/mariage/{idMariage}", method = GET)
	public MariageDTO chargeMariage(@PathVariable("idMariage") final Long idMariage) {
		final Mariage mariage = this.mariageService.chargeMariageParId(idMariage);
		return new MariageDTO(mariage);
	}

	@RequestMapping(value = "/mariage", method = GET)
	public Collection<MariageDTO> listeTousMariages() {
		final Collection<Mariage> mariages = this.mariageService.listeTousMariages();
		return AbstractDTO.creerDto(mariages, MariageDTO.class);
	}

	@RequestMapping(value = "/mariage", method = POST)
	public Long sauvegardeMariage(//
			@RequestParam(required = false, value = "id") final Long id, //
			@RequestParam(value = "dateCelebration") final String dateCelebration, //
			@RequestParam(value = "marie1") final String marie1, //
			@RequestParam(value = "marie2") final String marie2) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(AbstractDTO.FORMAT_DATE);
			return this.mariageService.sauvegarde(new Mariage(id, sdf.parse(dateCelebration), marie1, marie2));
		} catch (final ParseException e) {
			LOG.error("Erreur de format des paramètres d'entrée", e);
			throw new RestException(RestException.ERREUR_FORMAT_DATE, e,
					new String[] { AbstractDTO.FORMAT_DATE, dateCelebration });
		}
	}

}
