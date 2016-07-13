package com.github.talbotgui.mariage.metier.dto;

import java.util.Map;

public class StatistiquesRepartitionsInvitesMariage {

	private final Map<String, Integer> nbFoyersParEtape;
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
		this.nbFoyersParEtape = null;
	}

	public StatistiquesRepartitionsInvitesMariage(final Map<String, Integer> nbParAge,
			final Map<String, Integer> nbParFoyer, final Map<String, Integer> nbParGroupe,
			final Map<String, Integer> nbParEtape, final Map<String, Integer> nbFoyersParEtape) {
		super();
		this.nbParAge = nbParAge;
		this.nbParFoyer = nbParFoyer;
		this.nbParGroupe = nbParGroupe;
		this.nbParEtape = nbParEtape;
		this.nbFoyersParEtape = nbFoyersParEtape;
	}

	public final Map<String, Integer> getNbFoyersParEtape() {
		return nbFoyersParEtape;
	}

	public final Map<String, Integer> getNbParAge() {
		return nbParAge;
	}

	public final Map<String, Integer> getNbParEtape() {
		return nbParEtape;
	}

	public final Map<String, Integer> getNbParFoyer() {
		return nbParFoyer;
	}

	public final Map<String, Integer> getNbParGroupe() {
		return nbParGroupe;
	}
}
