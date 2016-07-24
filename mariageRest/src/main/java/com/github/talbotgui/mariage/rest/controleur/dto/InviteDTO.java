package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;

import com.github.talbotgui.mariage.metier.entities.Invite;

public class InviteDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String age;
	private String foyer;
	private String groupe;
	private Long id;
	private Long idFoyer;
	private String nom;
	private String prenom;

	public InviteDTO() {
		super();
	}

	public InviteDTO(final Invite i) {
		if (i.getAge() != null) {
			this.age = i.getAge().toString();
		}
		this.id = i.getId();
		this.nom = i.getNom();
		this.prenom = i.getPrenom();
		if (i.getFoyer() != null) {
			this.foyer = i.getFoyer().getNom();
			this.idFoyer = i.getFoyer().getId();
			this.groupe = i.getFoyer().getGroupe();
		}
	}

	public InviteDTO(final Object i) {
		this((Invite) i);
	}

	public String getAge() {
		return this.age;
	}

	public String getFoyer() {
		return this.foyer;
	}

	public String getGroupe() {
		return this.groupe;
	}

	public Long getId() {
		return this.id;
	}

	public Long getIdFoyer() {
		return this.idFoyer;
	}

	public String getNom() {
		return this.nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setAge(final String age) {
		this.age = age;
	}

	public void setFoyer(final String foyer) {
		this.foyer = foyer;
	}

	public void setGroupe(final String groupe) {
		this.groupe = groupe;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setIdFoyer(final Long idFoyer) {
		this.idFoyer = idFoyer;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

}
