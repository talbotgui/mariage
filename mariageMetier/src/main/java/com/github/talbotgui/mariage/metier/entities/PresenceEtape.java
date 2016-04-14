package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PresenceEtape implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ETAPE_ID")
	private Etape etape;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "INVITE_ID")
	private Invite invite;

	private Boolean present;

	protected PresenceEtape() {
		super();
	}

	public PresenceEtape(final Etape etape, final Boolean present) {
		super();
		this.etape = etape;
		this.present = present;
	}

	public PresenceEtape(final Etape etape, final Invite invite, final Boolean present) {
		super();
		this.etape = etape;
		this.invite = invite;
		this.present = present;
	}

	public Etape getEtape() {
		return etape;
	}

	public Long getId() {
		return id;
	}

	public Invite getInvite() {
		return invite;
	}

	public Boolean getPresent() {
		return present;
	}

	public void setEtape(final Etape etape) {
		this.etape = etape;
	}

	public void setInvite(final Invite invite) {
		this.invite = invite;
	}

	public void setPresent(final Boolean present) {
		this.present = present;
	}

}
