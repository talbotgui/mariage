package com.github.talbotgui.mariage.metier.dto;

import java.util.Map;

public class StatistiquesRepartitionsInvitesMariage {

	private final Map<String, Integer> nbParAge;
	private final Map<String, Integer> nbParEtape;
	private final Map<String, Integer> nbParFoyer;
	private final Map<String, Integer> nbParGroupe;

	public StatistiquesRepartitionsInvitesMariage() {
		super();
		this.nbParAge = null;
		this.nbParFoyer = null;
		this.nbParGroupe = null;
		this.nbParEtape = null;
	}

	public StatistiquesRepartitionsInvitesMariage(final Map<String, Integer> nbParAge,
			final Map<String, Integer> nbParFoyer, final Map<String, Integer> nbParGroupe,
			final Map<String, Integer> nbParEtape) {
		super();
		this.nbParAge = nbParAge;
		this.nbParFoyer = nbParFoyer;
		this.nbParGroupe = nbParGroupe;
		this.nbParEtape = nbParEtape;
	}

	public Map<String, Integer> getNbParAge() {
		return nbParAge;
	}

	public Map<String, Integer> getNbParEtape() {
		return nbParEtape;
	}

	public Map<String, Integer> getNbParFoyer() {
		return nbParFoyer;
	}

	public Map<String, Integer> getNbParGroupe() {
		return nbParGroupe;
	}

}
