package com.github.talbotgui.mariage.metier.entities;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class EtapeCeremonie extends Etape {
	private static final long serialVersionUID = 1L;

	private String celebrant;

	protected EtapeCeremonie() {
		super();
	}

	public EtapeCeremonie(final Integer numOrdre, final String nom, final Date dateHeure, final String lieu) {
		super(numOrdre, nom, dateHeure, lieu);
	}

	public EtapeCeremonie(final Integer numOrdre, final String nom, final Date dateHeure, final String lieu,
			final String celebrant) {
		super(numOrdre, nom, dateHeure, lieu);
		this.setCelebrant(celebrant);
	}

	public EtapeCeremonie(final Long id, final String nom, final Date dateHeure, final String lieu,
			final String celebrant) {
		super(id, nom, dateHeure, lieu);
		this.setCelebrant(celebrant);
	}

	public String getCelebrant() {
		return this.celebrant;
	}

	public final void setCelebrant(final String celebrant) {
		this.celebrant = celebrant;
	}

}
