package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

@Entity
@Table(name = "EVENEMENT_PARTICIPANTS")
public class ParticipationEvenement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private ParticipationEvenementId id;

	protected ParticipationEvenement() {
		super();
	}

	public ParticipationEvenement(final Evenement evenement, final Utilisateur participant) {
		super();
		this.setId(new ParticipationEvenementId(evenement, participant));
	}

	public ParticipationEvenementId getId() {
		return this.id;
	}

	public final void setId(final ParticipationEvenementId id) {
		this.id = id;
	}

}
