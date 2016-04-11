package com.github.talbotgui.mariage.rest.controleur.dto;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public class UtilisateurDTO extends AbstractDTO {

	private String login;

	public UtilisateurDTO() {
		super(null);
	}

	public UtilisateurDTO(final Object entity) {
		super(entity);

		if (entity != null) {
			final Utilisateur u = (Utilisateur) entity;
			this.login = u.getLogin();
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

}
