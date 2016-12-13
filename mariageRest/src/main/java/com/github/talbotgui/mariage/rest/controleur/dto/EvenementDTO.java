package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.github.talbotgui.mariage.metier.entities.Evenement;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public class EvenementDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date debut;
	private Date fin;
	private Long id;
	private final Collection<String> participants = new ArrayList<>();
	private String titre;

	public EvenementDTO() {

	}

	public EvenementDTO(final Evenement evenement) {
		this.id = evenement.getId();
		this.titre = evenement.getTitre();
		this.debut = evenement.getDebut();
		this.fin = evenement.getFin();
		if (evenement.getParticipants() != null) {
			for (final Utilisateur u : evenement.getParticipants()) {
				this.participants.add(u.getLogin());
			}
		}
	}

	public EvenementDTO(final Object o) {
		this((Evenement) o);
	}

	public Date getDebut() {
		if (this.debut != null) {
			return new Date(this.debut.getTime());
		}
		return null;
	}

	public Date getFin() {
		if (this.fin != null) {
			return new Date(this.fin.getTime());
		}
		return null;
	}

	public Long getId() {
		return this.id;
	}

	public Collection<String> getParticipants() {
		return this.participants;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setDebut(final Date debut) {
		if (debut != null) {
			this.debut = new Date(debut.getTime());
		}
	}

	public void setFin(final Date fin) {
		if (fin != null) {
			this.fin = new Date(fin.getTime());
		}
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setTitre(final String titre) {
		this.titre = titre;
	}
}
