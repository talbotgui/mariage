package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

@Embeddable
public class ParticipationEvenementId implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "EVENEMENT")
	private Evenement evenement;

	@ManyToOne
	@JoinColumn(name = "PARTICIPANTS")
	private Utilisateur participant;

	protected ParticipationEvenementId() {
		super();
	}

	public ParticipationEvenementId(final Evenement evenement, final Utilisateur participant) {
		super();
		this.setEvenement(evenement);
		this.setParticipant(participant);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		return this.hashCode() == obj.hashCode();
	}

	public Evenement getEvenement() {
		return this.evenement;
	}

	public Utilisateur getParticipant() {
		return this.participant;
	}

	@Override
	public int hashCode() {
		final HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.evenement.getId());
		hcb.append(this.participant.getLogin());
		return hcb.toHashCode();
	}

	public void setEvenement(final Evenement evenement) {
		this.evenement = evenement;
	}

	public void setParticipant(final Utilisateur participant) {
		this.participant = participant;
	}

}
