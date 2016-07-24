package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public class UtilisateurDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String login;

	public UtilisateurDTO() {
		super();
	}

	public UtilisateurDTO(final Object u) {
		this((Utilisateur) u);
	}

	public UtilisateurDTO(final Utilisateur u) {
		this.login = u.getLogin();
	}

	public String getId() {
		return this.login;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

}
