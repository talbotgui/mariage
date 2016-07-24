package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FOYER_COURRIER_INVITATION")
public class FoyerCourrierInvitation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private FoyerCourrierInvitationId id;

	protected FoyerCourrierInvitation() {
		super();
	}

	public FoyerCourrierInvitation(final Courrier courrier, final Foyer foyer) {
		super();
		this.setId(new FoyerCourrierInvitationId(courrier, foyer));
	}

	public FoyerCourrierInvitationId getId() {
		return this.id;
	}

	public void setId(final FoyerCourrierInvitationId id) {
		this.id = id;
	}

}
