package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.service.MariageService;

@RestController
public class ParametreRestControler {

	@Autowired
	private MariageService mariageService;

	@RequestMapping(value = "/parametres/age", method = GET)
	public Collection<String> listeAges() {
		return this.mariageService.listeAgePossible();
	}
}
