package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;

import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;

public class AutorisationDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idMariage;
	private String login;
	private String nomMaries;

	public AutorisationDTO() {

	}

	public AutorisationDTO(final Autorisation autorisation) {
		this.id = autorisation.getId();
		this.login = autorisation.getUtilisateur().getLogin();
		this.idMariage = autorisation.getMariage().getId();
		this.nomMaries = autorisation.getMariage().getMarie1() + " & " + autorisation.getMariage().getMarie2();
	}

	public AutorisationDTO(final Object o) {
		this((Autorisation) o);
	}

	public Long getId() {
		return this.id;
	}

	public Long getIdMariage() {
		return this.idMariage;
	}

	public String getLogin() {
		return this.login;
	}

	public String getNomMaries() {
		return this.nomMaries;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setIdMariage(final Long idMariage) {
		this.idMariage = idMariage;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public void setNomMaries(final String nomMaries) {
		this.nomMaries = nomMaries;
	}
}
