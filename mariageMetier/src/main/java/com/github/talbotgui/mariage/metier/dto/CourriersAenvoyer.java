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
		return adresse;
	}

	public String getFoyer() {
		return foyer;
	}

	public String getGroupe() {
		return groupe;
	}

	public int getNbEtapes() {
		return nbEtapes;
	}

	public String getNomCourrier() {
		return nomCourrier;
	}

	@Override
	public String toString() {
		return "CourriersAenvoyer [adresse=" + adresse + ", foyer=" + foyer + ", groupe=" + groupe + ", nbEtapes="
				+ nbEtapes + ", nomCourrier=" + nomCourrier + "]";
	}

}
