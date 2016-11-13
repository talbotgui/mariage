package com.github.talbotgui.mariage.metier.dto;

public class StatistiquesPresenceMariage {

	private Long idEtape;
	private long nbAbsence;
	private long nbAbsenceConfirme;
	private long nbInconnu;
	private long nbPresence;
	private long nbPresenceConfirme;
	private String nomEtape;

	public StatistiquesPresenceMariage() {
		super();
	}

	public StatistiquesPresenceMariage(final Long idEtape, final String nomEtape, final long nbAbsence,
			final long nbAbsenceConfirme, final long nbPresence, final long nbPresenceConfirme, final long nbTotal) {
		super();
		this.idEtape = idEtape;
		this.nomEtape = nomEtape;
		this.nbAbsence = nbAbsence;
		this.nbAbsenceConfirme = nbAbsenceConfirme;
		this.nbPresence = nbPresence;
		this.nbPresenceConfirme = nbPresenceConfirme;
		this.nbInconnu = nbTotal - nbAbsence - nbPresence;
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

	public String getNomEtape() {
		return this.nomEtape;
	}

	public void setIdEtape(final Long idEtape) {
		this.idEtape = idEtape;
	}

	public void setNbAbsence(final long nbAbsence) {
		this.nbAbsence = nbAbsence;
	}

	public void setNbAbsenceConfirme(final long nbAbsenceConfirme) {
		this.nbAbsenceConfirme = nbAbsenceConfirme;
	}

	public void setNbInconnu(final long nbInconnu) {
		this.nbInconnu = nbInconnu;
	}

	public void setNbPresence(final long nbPresence) {
		this.nbPresence = nbPresence;
	}

	public void setNbPresenceConfirme(final long nbPresenceConfirme) {
		this.nbPresenceConfirme = nbPresenceConfirme;
	}

	public void setNomEtape(final String nomEtape) {
		this.nomEtape = nomEtape;
	}

}
