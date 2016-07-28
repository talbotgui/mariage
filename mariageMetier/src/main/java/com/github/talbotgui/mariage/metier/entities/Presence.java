package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRESENCE")
public class Presence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private PresenceId id;

	protected Presence() {
		super();
	}

	public Presence(final Etape etape, final Invite invite) {
		super();
		this.setId(new PresenceId(etape, invite));
	}

	public PresenceId getId() {
		return this.id;
	}

	public void setId(final PresenceId id) {
		this.id = id;
	}

}
