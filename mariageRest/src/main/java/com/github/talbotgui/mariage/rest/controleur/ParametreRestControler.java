package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.talbotgui.mariage.metier.service.MariageService;
import com.github.talbotgui.mariage.metier.service.SecuriteService;

@RestController
public class ParametreRestControler {

	@Autowired
	private MariageService mariageService;

	@Autowired
	private SecuriteService securiteService;

	@RequestMapping(value = "/parametres/age", method = GET)
	public Collection<String> listeAges() {
		return this.mariageService.listeAgePossible();
	}

	@RequestMapping(value = "/parametres/role", method = GET)
	public Collection<String> listeRoles() {
		return this.securiteService.listeRolePossible();
	}
}
