package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		return this.hashCode() == obj.hashCode();
	}

	public Courrier getCourrier() {
		return this.courrier;
	}

	public Foyer getFoyer() {
		return this.foyer;
	}

	@Override
	public int hashCode() {
		final HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(this.courrier.getId());
		hcb.append(this.foyer.getId());
		return hcb.toHashCode();
	}

	public void setCourrier(final Courrier courrier) {
		this.courrier = courrier;
	}

	public void setFoyer(final Foyer foyer) {
		this.foyer = foyer;
	}

}
