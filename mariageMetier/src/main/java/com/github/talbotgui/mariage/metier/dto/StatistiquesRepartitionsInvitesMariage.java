package com.github.talbotgui.mariage.metier.dto;

import java.util.Map;

public class StatistiquesRepartitionsInvitesMariage {

	private final Map<String, Integer> nbParAge;
	private final Map<String, Integer> nbParFoyer;
	private final Map<String, Integer> nbParGroupe;

	public StatistiquesRepartitionsInvitesMariage() {
		super();
		this.nbParAge = null;
		this.nbParFoyer = null;
		this.nbParGroupe = null;
	}

	public StatistiquesRepartitionsInvitesMariage(final Map<String, Integer> nbParAge,
			final Map<String, Integer> nbParFoyer, final Map<String, Integer> nbParGroupe) {
		super();
		this.nbParAge = nbParAge;
		this.nbParFoyer = nbParFoyer;
		this.nbParGroupe = nbParGroupe;
	}

	public Map<String, Integer> getNbParAge() {
		return nbParAge;
	}

	public Map<String, Integer> getNbParFoyer() {
		return nbParFoyer;
	}

	public Map<String, Integer> getNbParGroupe() {
		return nbParGroupe;
	}

}
