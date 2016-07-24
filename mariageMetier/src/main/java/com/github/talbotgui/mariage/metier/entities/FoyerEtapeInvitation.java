package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FOYER_ETAPE_INVITATION")
public class FoyerEtapeInvitation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private FoyerEtapeInvitationId id;

	protected FoyerEtapeInvitation() {
		super();
	}

	public FoyerEtapeInvitation(final Etape etape, final Foyer foyer) {
		super();
		this.setId(new FoyerEtapeInvitationId(etape, foyer));
	}

	public FoyerEtapeInvitationId getId() {
		return this.id;
	}

	public void setId(final FoyerEtapeInvitationId id) {
		this.id = id;
	}

}
