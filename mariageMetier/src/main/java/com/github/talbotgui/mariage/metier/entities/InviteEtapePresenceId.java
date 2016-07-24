package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class InviteEtapePresenceId implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "ETAPE_ID")
	private Etape etape;

	@ManyToOne
	@JoinColumn(name = "INVITE_ID")
	private Invite invite;

	protected InviteEtapePresenceId() {
		super();
	}

	public InviteEtapePresenceId(final Etape etape, final Invite invite) {
		super();
		this.setEtape(etape);
		this.setInvite(invite);
	}

	public Etape getEtape() {
		return this.etape;
	}

	public Invite getInvite() {
		return this.invite;
	}

	public void setEtape(final Etape etape) {
		this.etape = etape;
	}

	public void setInvite(final Invite invite) {
		this.invite = invite;
	}

}
