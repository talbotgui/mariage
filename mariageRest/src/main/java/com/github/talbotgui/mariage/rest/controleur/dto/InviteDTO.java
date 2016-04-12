package com.github.talbotgui.mariage.rest.controleur.dto;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Hibernate;

import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.PresenceEtape;

public class InviteDTO extends AbstractDTO {

	private String age;
	private String foyer;
	private String groupe;
	private Long id;

	private String nom;

	private String prenom;
	private Map<Long, Boolean> presencesEtape = new HashMap<>();

	public InviteDTO() {
		super(null);
	}

	public InviteDTO(final Object entity) {
		super(entity);

		if (entity != null) {
			final Invite i = (Invite) entity;
			this.id = i.getId();
			this.groupe = i.getGroupe();
			this.foyer = i.getFoyer();
			this.nom = i.getNom();
			this.prenom = i.getPrenom();
			if (i.getAge() != null) {
				this.age = i.getAge().toString();
			}

			if (i.getPresencesEtape() != null && Hibernate.isInitialized(i.getPresencesEtape())) {
				for (final PresenceEtape pe : i.getPresencesEtape()) {
					this.presencesEtape.put(pe.getEtape().getId(), pe.getPresent());
				}
			}
		}
	}

	public String getAge() {
		return age;
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

	public Map<Long, Boolean> getPresencesEtape() {
		return presencesEtape;
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

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

	public void setPresencesEtape(final Map<Long, Boolean> presencesEtape) {
		this.presencesEtape = presencesEtape;
	}
}
