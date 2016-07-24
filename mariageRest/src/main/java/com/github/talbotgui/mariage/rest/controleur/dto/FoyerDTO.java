package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;

import com.github.talbotgui.mariage.metier.entities.Foyer;

public class FoyerDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String adresse;
	private String email;
	private String groupe;
	private Long id;
	private String nom;
	private String telephone;

	public FoyerDTO() {

	}

	public FoyerDTO(final Foyer foyer) {
		this.adresse = foyer.getAdresse();
		this.email = foyer.getEmail();
		this.groupe = foyer.getGroupe();
		this.id = foyer.getId();
		this.nom = foyer.getNom();
		this.telephone = foyer.getTelephone();
	}

	public FoyerDTO(final Object o) {
		this((Foyer) o);
	}

	public String getAdresse() {
		return this.adresse;
	}

	public String getEmail() {
		return this.email;
	}

	public String getGroupe() {
		return this.groupe;
	}

	public Long getId() {
		return this.id;
	}

	public String getNom() {
		return this.nom;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setAdresse(final String adresse) {
		this.adresse = adresse;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setGroupe(final String groupe) {
		this.groupe = groupe;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

}
