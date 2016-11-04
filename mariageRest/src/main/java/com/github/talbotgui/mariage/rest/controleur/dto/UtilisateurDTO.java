package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public class UtilisateurDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String login;

	private String role;

	private Boolean verrouille;

	public UtilisateurDTO() {
		super();
	}

	public UtilisateurDTO(final Object u) {
		this((Utilisateur) u);
	}

	public UtilisateurDTO(final Utilisateur u) {
		this.login = u.getLogin();
		this.verrouille = u.isVerrouille();
		if (u.getRole() != null) {
			this.role = u.getRole().toString();
		}
	}

	public String getId() {
		return this.login;
	}

	public String getLogin() {
		return this.login;
	}

	public String getRole() {
		return this.role;
	}

	public Boolean getVerrouille() {
		return this.verrouille;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public void setVerrouille(final Boolean verrouille) {
		this.verrouille = verrouille;
	}

}
