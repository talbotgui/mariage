package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INVITE_ETAPE_PRESENCE")
public class InviteEtapePresence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private InviteEtapePresenceId id;

	protected InviteEtapePresence() {
		super();
	}

	public InviteEtapePresence(final Etape etape, final Invite invite) {
		super();
		this.setId(new InviteEtapePresenceId(etape, invite));
	}

	public InviteEtapePresenceId getId() {
		return this.id;
	}

	public void setId(final InviteEtapePresenceId id) {
		this.id = id;
	}

}
