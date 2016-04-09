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

	public EtapeCeremonie(final Long id, final String nom, final Date dateHeure, final String lieu,
			final String celebrant) {
		super(id, nom, dateHeure, lieu);
		this.celebrant = celebrant;
	}

	public EtapeCeremonie(final String nom, final Date dateHeure, final String lieu) {
		super(nom, dateHeure, lieu);
	}

	public EtapeCeremonie(final String nom, final Date dateHeure, final String lieu, final String celebrant) {
		super(nom, dateHeure, lieu);
		this.celebrant = celebrant;
	}

	public String getCelebrant() {
		return celebrant;
	}

	public void setCelebrant(final String celebrant) {
		this.celebrant = celebrant;
	}

}
