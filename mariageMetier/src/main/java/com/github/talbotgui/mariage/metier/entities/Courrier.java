package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Courrier implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date datePrevisionEnvoi;

	@ManyToMany
	private Collection<Etape> etapes = new ArrayList<>();

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
		return this.datePrevisionEnvoi;
	}

	public Collection<Etape> getEtapes() {
		return new ArrayList<>(this.etapes);
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
		this.datePrevisionEnvoi = datePrevisionEnvoi;
	}

	public void setEtapes(final Collection<Etape> etapes) {
		this.etapes = new ArrayList<>(etapes);
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
