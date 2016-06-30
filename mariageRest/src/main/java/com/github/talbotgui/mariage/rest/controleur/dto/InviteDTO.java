package com.github.talbotgui.mariage.rest.controleur.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Hibernate;

import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.PresenceEtape;

public class InviteDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;

	private String adresse;
	private String age;
	private String email;

	private String foyer;

	private String groupe;
	private Long id;
	private String nom;
	private String prenom;
	private final Collection<PresenceEtapeDTO> presencesEtape = new ArrayList<>();
	private String telephone;

	public InviteDTO() {
		super(null);
	}

	public InviteDTO(final Object entity) {
		super(entity);

		if (entity != null) {
			final Invite i = (Invite) entity;
			this.adresse = i.getAdresse();
			this.email = i.getEmail();
			if (i.getAge() != null) {
				this.age = i.getAge().toString();
			}
			this.foyer = i.getFoyer();
			this.groupe = i.getGroupe();
			this.id = i.getId();
			this.nom = i.getNom();
			this.prenom = i.getPrenom();
			if (i.getPresencesEtape() != null && Hibernate.isInitialized(i.getPresencesEtape())) {
				for (final PresenceEtape pe : i.getPresencesEtape()) {
					this.presencesEtape.add(new PresenceEtapeDTO(pe));
				}
			}
			this.telephone = i.getTelephone();
		}
	}

	public String getAdresse() {
		return adresse;
	}

	public String getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}

	public String getFoyer() {
		return foyer;
	}

	public String getGroupe() {
		return groupe;
	}

	public Long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public Collection<PresenceEtapeDTO> getPresencesEtape() {
		return presencesEtape;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setAdresse(final String adresse) {
		this.adresse = adresse;
	}

	public void setAge(final String age) {
		this.age = age;
	}

	public void setEmail(final String email) {
		this.email = email;
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

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}
}
