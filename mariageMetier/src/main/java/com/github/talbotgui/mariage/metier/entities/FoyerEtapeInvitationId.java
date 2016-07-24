package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class FoyerEtapeInvitationId implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "ETAPE_ID")
	private Etape etape;

	@ManyToOne
	@JoinColumn(name = "FOYER_ID")
	private Foyer foyer;

	protected FoyerEtapeInvitationId() {
		super();
	}

	public FoyerEtapeInvitationId(final Etape etape, final Foyer foyer) {
		super();
		this.setEtape(etape);
		this.setFoyer(foyer);
	}

	public Etape getEtape() {
		return this.etape;
	}

	public Foyer getFoyer() {
		return this.foyer;
	}

	public void setEtape(final Etape etape) {
		this.etape = etape;
	}

	public void setFoyer(final Foyer foyer) {
		this.foyer = foyer;
	}

}
