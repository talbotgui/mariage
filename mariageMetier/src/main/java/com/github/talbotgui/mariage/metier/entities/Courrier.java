package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Courrier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "DATE_PREVISION_ENVOI")
	private Date datePrevisionEnvoi;

	@ManyToMany
	@JoinTable(name = "COURRIER_ETAPES", //
			joinColumns = { @JoinColumn(name = "COURRIER", insertable = false, updatable = false) }, //
			inverseJoinColumns = { @JoinColumn(name = "ETAPES", insertable = false, updatable = false) }//
	)
	private Collection<Etape> etapes = new ArrayList<>();

	@ManyToMany(mappedBy = "courriersInvitation")
	private Collection<Foyer> foyersInvites;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "MARIAGE_ID")
	private Mariage mariage;

	private String nom;

	public Courrier() {
		super();
	}

	public Courrier(final Long id) {
		super();
		this.id = id;
	}

	public Courrier(final Long id, final String nom, final Date datePrevisionEnvoi) {
		this(nom, datePrevisionEnvoi);
		this.setId(id);
	}

	public Courrier(final String nom, final Date datePrevisionEnvoi) {
		super();
		this.setNom(nom);
		this.setDatePrevisionEnvoi(datePrevisionEnvoi);
	}

	public void addEtape(final Etape etapeInvitation) {
		this.etapes.add(etapeInvitation);
	}

	public Date getDatePrevisionEnvoi() {
		if (this.datePrevisionEnvoi != null) {
			return new Date(this.datePrevisionEnvoi.getTime());
		}
		return null;
	}

	public Collection<Etape> getEtapes() {
		return new ArrayList<>(this.etapes);
	}

	public Collection<Foyer> getFoyersInvites() {
		return new ArrayList<>(this.foyersInvites);
	}

	public Long getId() {
		return this.id;
	}

	public Mariage getMariage() {
		return this.mariage;
	}

	public String getNom() {
		return this.nom;
	}

	public void removeEtape(final Etape e) {
		this.etapes.remove(e);
	}

	public void setDatePrevisionEnvoi(final Date datePrevisionEnvoi) {
		if (datePrevisionEnvoi != null) {
			this.datePrevisionEnvoi = new Date(datePrevisionEnvoi.getTime());
		}
	}

	public void setEtapes(final Collection<Etape> etapes) {
		this.etapes = new ArrayList<>(etapes);
	}

	public void setFoyersInvites(final Collection<Foyer> foyersInvites) {
		this.foyersInvites = new ArrayList<>(foyersInvites);
	}

	private void setId(final Long id) {
		this.id = id;
	}

	public void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return "Courrier [id=" + this.id + ", nom=" + this.nom + ", datePrevisionEnvoi=" + this.datePrevisionEnvoi
				+ "]";
	}

}
