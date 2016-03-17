package com.github.talbotgui.mariage.metier.entities;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class EtapeCeremonie extends Etape {

	private String celebrant;

	protected EtapeCeremonie() {
		super();
	}

	public EtapeCeremonie(String nom, Date dateHeure, String lieu) {
		super(nom, dateHeure, lieu);
	}

	public EtapeCeremonie(String nom, Date dateHeure, String lieu, String celebrant) {
		super(nom, dateHeure, lieu);
		this.celebrant = celebrant;
	}

	public String getCelebrant() {
		return celebrant;
	}

	public void setCelebrant(String celebrant) {
		this.celebrant = celebrant;
	}

}
