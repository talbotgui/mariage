package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INVITATION")
public class Invitation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private InvitationId id;

	protected Invitation() {
		super();
	}

	public Invitation(final Courrier courrier, final Foyer foyer) {
		super();
		this.setId(new InvitationId(courrier, foyer));
	}

	public InvitationId getId() {
		return this.id;
	}

	public void setId(final InvitationId id) {
		this.id = id;
	}

}
