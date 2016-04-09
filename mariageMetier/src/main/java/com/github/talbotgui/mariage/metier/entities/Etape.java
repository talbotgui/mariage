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

	@OneToMany(mappedBy = "etape", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	private Collection<PresenceEtape> presences;

	protected Etape() {
		super();
	}

	public Etape(Long id, String nom, Date dateHeure, String lieu) {
		super();
		this.id = id;
		this.nom = nom;
		if (dateHeure != null) {
			this.dateHeure = new Date(dateHeure.getTime());
		}
		this.lieu = lieu;
	}

	public Etape(String nom, Date dateHeure, String lieu) {
		super();
		if (dateHeure != null) {
			this.dateHeure = new Date(dateHeure.getTime());
		}
		this.nom = nom;
		this.lieu = lieu;
	}

	public Date getDateHeure() {
		if (this.dateHeure != null) {
			return new Date(this.dateHeure.getTime());
		}
		return dateHeure;
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

	public void setDateHeure(Date dateHeure) {
		if (dateHeure != null) {
			this.dateHeure = new Date(dateHeure.getTime());
		}
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public void setMariage(Mariage mariage) {
		this.mariage = mariage;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
