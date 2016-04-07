package com.github.talbotgui.mariage.metier.entities;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class EtapeRepas extends Etape {

	protected EtapeRepas() {
		super();
	}

	public EtapeRepas(Long id, String nom, Date dateHeure, String lieu) {
		super(id, nom, dateHeure, lieu);
	}

	public EtapeRepas(String nom, Date dateHeure, String lieu) {
		super(nom, dateHeure, lieu);
	}

}
