package com.github.talbotgui.mariage.metier.entities;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class EtapeRepas extends Etape {
	private static final long serialVersionUID = 1L;

	protected EtapeRepas() {
		super();
	}

	public EtapeRepas(final Integer numOrdre, final String nom, final Date dateHeure, final String lieu) {
		super(numOrdre, nom, dateHeure, lieu);
	}

	public EtapeRepas(final Long id) {
		super(id);
	}

	public EtapeRepas(final Long id, final String nom, final Date dateHeure, final String lieu) {
		super(id, nom, dateHeure, lieu);
	}

}
