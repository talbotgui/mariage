package com.github.talbotgui.mariage.metier.dto;

public class StatistiquesMariage {
	private final Integer nbFoyers;
	private final Integer nbGroupes;
	private final Integer nbInvites;
	private final Integer nbInvitesIncomplets;

	private final Integer nbInvitesSansAdresse;
	private final Integer nbInvitesSansAge;

	public StatistiquesMariage(final Long nbFoyers, final Long nbGroupes, final Integer nbInvites,
			final Integer nbInvitesIncomplets, final Integer nbInvitesSansAdresse, final Integer nbInvitesSansAge) {
		super();
		this.nbFoyers = nbFoyers.intValue();
		this.nbGroupes = nbGroupes.intValue();
		this.nbInvites = nbInvites;
		this.nbInvitesIncomplets = nbInvitesIncomplets;
		this.nbInvitesSansAdresse = nbInvitesSansAdresse;
		this.nbInvitesSansAge = nbInvitesSansAge;
	}

	public Integer getNbFoyers() {
		return nbFoyers;
	}

	public Integer getNbGroupes() {
		return nbGroupes;
	}

	public Integer getNbInvites() {
		return nbInvites;
	}

	public Integer getNbInvitesIncomplets() {
		return nbInvitesIncomplets;
	}

	public Integer getNbInvitesSansAdresse() {
		return nbInvitesSansAdresse;
	}

	public Integer getNbInvitesSansAge() {
		return nbInvitesSansAge;
	}
}
