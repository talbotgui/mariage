package com.github.talbotgui.mariage.metier.dto;

public class StatistiquesMariage {

	private final StatistiquesInvitesMariage invites;

	private final StatistiquesRepartitionsInvitesMariage repartitions;

	public StatistiquesMariage() {
		super();
		this.invites = null;
		this.repartitions = null;
	}

	public StatistiquesMariage(final StatistiquesRepartitionsInvitesMariage repartitions,
			final StatistiquesInvitesMariage invites) {
		super();
		this.repartitions = repartitions;
		this.invites = invites;
	}

	public StatistiquesInvitesMariage getInvites() {
		return invites;
	}

	public StatistiquesRepartitionsInvitesMariage getRepartitions() {
		return repartitions;
	}

}
