package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class PresenceId implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "ETAPE_ID")
	private Etape etape;

	@ManyToOne
	@JoinColumn(name = "INVITE_ID")
	private Invite invite;

	protected PresenceId() {
		super();
	}

	public PresenceId(final Etape etape, final Invite invite) {
		super();
		this.setEtape(etape);
		this.setInvite(invite);
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

	public Etape getEtape() {
		return this.etape;
	}

	public Invite getInvite() {
		return this.invite;
	}

	@Override
	public int hashCode() {
		final HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.etape.getId());
		hcb.append(this.invite.getId());
		return hcb.toHashCode();
	}

	public final void setEtape(final Etape etape) {
		this.etape = etape;
	}

	public final void setInvite(final Invite invite) {
		this.invite = invite;
	}

}
