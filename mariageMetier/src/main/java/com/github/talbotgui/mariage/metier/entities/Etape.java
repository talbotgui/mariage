package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Etape implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date dateHeure;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String lieu;

	@ManyToOne
	@JoinColumn(name = "MARIAGE_ID")
	private Mariage mariage;

	private String nom;

	private Integer numOrdre;

	@OneToMany(mappedBy = "etape", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	private Collection<PresenceEtape> presences;

	protected Etape() {
		super();
	}

	public Etape(final Integer numOrdre, final String nom, final Date dateHeure, final String lieu) {
		super();
		this.setNumOrdre(numOrdre);
		if (dateHeure != null) {
			this.setDateHeure(dateHeure);
		}
		this.setNom(nom);
		this.setLieu(lieu);
	}

	public Etape(final Long id, final String nom, final Date dateHeure, final String lieu) {
		super();
		this.setId(id);
		this.setNom(nom);
		if (dateHeure != null) {
			this.setDateHeure(dateHeure);
		}
		this.setLieu(lieu);
	}

	public Date getDateHeure() {
		if (this.dateHeure != null) {
			return new Date(this.dateHeure.getTime());
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public String getLieu() {
		return lieu;
	}

	public Mariage getMariage() {
		return mariage;
	}

	public String getNom() {
		return nom;
	}

	public Integer getNumOrdre() {
		return numOrdre;
	}

	public Collection<PresenceEtape> getPresences() {
		return presences;
	}

	public void setDateHeure(final Date dateHeure) {
		if (dateHeure != null) {
			this.dateHeure = new Date(dateHeure.getTime());
		}
	}

	private void setId(final Long id) {
		this.id = id;
	}

	public void setLieu(final String lieu) {
		this.lieu = lieu;
	}

	public void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setNumOrdre(final Integer numOrdre) {
		this.numOrdre = numOrdre;
	}

	public void setPresences(final Collection<PresenceEtape> presences) {
		this.presences = presences;
	}

}
