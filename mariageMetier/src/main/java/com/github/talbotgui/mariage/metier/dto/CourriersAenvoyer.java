package com.github.talbotgui.mariage.metier.dto;

public class CourriersAenvoyer {

	private final String adresse;
	private final String foyer;
	private final String groupe;
	private final int nbEtapes;
	private final String nomCourrier;

	public CourriersAenvoyer(final String groupe, final String foyer, final String adresse, final String nomCourrier,
			final int nbEtapes) {
		super();
		this.groupe = groupe;
		this.foyer = foyer;
		this.adresse = adresse;
		this.nomCourrier = nomCourrier;
		this.nbEtapes = nbEtapes;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public String getFoyer() {
		return this.foyer;
	}

	public String getGroupe() {
		return this.groupe;
	}

	public int getNbEtapes() {
		return this.nbEtapes;
	}

	public String getNomCourrier() {
		return this.nomCourrier;
	}

	@Override
	public String toString() {
		return "CourriersAenvoyer [adresse=" + this.adresse + ", foyer=" + this.foyer + ", groupe=" + this.groupe
				+ ", nbEtapes=" + this.nbEtapes + ", nomCourrier=" + this.nomCourrier + "]";
	}

}
