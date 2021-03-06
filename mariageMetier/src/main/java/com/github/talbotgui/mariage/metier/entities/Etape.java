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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Etape implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "DATE_HEURE")
	private Date dateHeure;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToMany(mappedBy = "etapesPresence")
	private Collection<Invite> invitesPresents;

	private String lieu;

	@ManyToOne
	@JoinColumn(name = "MARIAGE_ID")
	private Mariage mariage;

	private String nom;

	@Column(name = "NUM_ORDRE")
	private Integer numOrdre;

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

	public Etape(final Long id) {
		super();
		this.setId(id);
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
		return this.id;
	}

	public Collection<Invite> getInvitesPresents() {
		return new ArrayList<>(this.invitesPresents);
	}

	public String getLieu() {
		return this.lieu;
	}

	public Mariage getMariage() {
		return this.mariage;
	}

	public String getNom() {
		return this.nom;
	}

	public Integer getNumOrdre() {
		return this.numOrdre;
	}

	public final void setDateHeure(final Date dateHeure) {
		if (dateHeure != null) {
			this.dateHeure = new Date(dateHeure.getTime());
		}
	}

	public final void setFoyersPresents(final Collection<Invite> invitesPresents) {
		this.invitesPresents = new ArrayList<>(invitesPresents);
	}

	private final void setId(final Long id) {
		this.id = id;
	}

	public final void setLieu(final String lieu) {
		this.lieu = lieu;
	}

	public final void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public final void setNom(final String nom) {
		this.nom = nom;
	}

	public final void setNumOrdre(final Integer numOrdre) {
		this.numOrdre = numOrdre;
	}

}
