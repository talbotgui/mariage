package com.github.talbotgui.mariage.metier.dto;

public class StatistiquesInvitesMariage {
	private final Integer nbFoyers;
	private final Integer nbGroupes;
	private final Integer nbInvites;

	private final Integer nbInvitesIncomplets;
	private final Integer nbInvitesSansAdresse;
	private final Integer nbInvitesSansAge;

	public StatistiquesInvitesMariage() {
		super();
		this.nbFoyers = null;
		this.nbGroupes = null;
		this.nbInvites = null;
		this.nbInvitesIncomplets = null;
		this.nbInvitesSansAdresse = null;
		this.nbInvitesSansAge = null;
	}

	public StatistiquesInvitesMariage(final Long nbFoyers, final Long nbGroupes, final Integer nbInvites,
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
