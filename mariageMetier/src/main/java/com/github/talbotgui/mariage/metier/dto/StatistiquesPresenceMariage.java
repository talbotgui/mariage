package com.github.talbotgui.mariage.metier.dto;

import com.github.talbotgui.mariage.metier.entities.Age;

public class StatistiquesPresenceMariage {

	private Long idEtape;
	private long nbAbsence;
	private long nbAbsenceConfirme;
	private long nbInconnu;
	private long nbPresence;
	private long nbPresenceConfirme;
	private String nomAge;

	public StatistiquesPresenceMariage() {
		super();
	}

	public StatistiquesPresenceMariage(final Long idEtape, final Age nomAge, final long nbAbsence,
			final long nbAbsenceConfirme, final long nbPresence, final long nbPresenceConfirme, final long nbTotal) {
		super();
		this.setIdEtape(idEtape);
		if (nomAge != null) {
			this.setNomAge(nomAge.name());
		}
		this.setNbAbsence(nbAbsence);
		this.setNbAbsenceConfirme(nbAbsenceConfirme);
		this.setNbPresence(nbPresence);
		this.setNbPresenceConfirme(nbPresenceConfirme);
		this.setNbInconnu(nbTotal - nbAbsence - nbPresence);

	}

	public Long getIdEtape() {
		return this.idEtape;
	}

	public long getNbAbsence() {
		return this.nbAbsence;
	}

	public long getNbAbsenceConfirme() {
		return this.nbAbsenceConfirme;
	}

	public long getNbInconnu() {
		return this.nbInconnu;
	}

	public long getNbPresence() {
		return this.nbPresence;
	}

	public long getNbPresenceConfirme() {
		return this.nbPresenceConfirme;
	}

	public String getNomAge() {
		return this.nomAge;
	}

	public final void setIdEtape(final Long idEtape) {
		this.idEtape = idEtape;
	}

	public final void setNbAbsence(final long nbAbsence) {
		this.nbAbsence = nbAbsence;
	}

	public final void setNbAbsenceConfirme(final long nbAbsenceConfirme) {
		this.nbAbsenceConfirme = nbAbsenceConfirme;
	}

	public final void setNbInconnu(final long nbInconnu) {
		this.nbInconnu = nbInconnu;
	}

	public final void setNbPresence(final long nbPresence) {
		this.nbPresence = nbPresence;
	}

	public final void setNbPresenceConfirme(final long nbPresenceConfirme) {
		this.nbPresenceConfirme = nbPresenceConfirme;
	}

	public final void setNomAge(final String nomAge) {
		this.nomAge = nomAge;
	}

}
