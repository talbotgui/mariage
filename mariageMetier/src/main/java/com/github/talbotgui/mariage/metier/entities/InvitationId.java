package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class InvitationId implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "COURRIER_ID")
	private Courrier courrier;

	@ManyToOne
	@JoinColumn(name = "FOYER_ID")
	private Foyer foyer;

	protected InvitationId() {
		super();
	}

	public InvitationId(final Courrier courrier, final Foyer foyer) {
		super();
		this.setCourrier(courrier);
		this.setFoyer(foyer);
	}

	public Courrier getCourrier() {
		return this.courrier;
	}

	public Foyer getFoyer() {
		return this.foyer;
	}

	public void setCourrier(final Courrier courrier) {
		this.courrier = courrier;
	}

	public void setFoyer(final Foyer foyer) {
		this.foyer = foyer;
	}

}
