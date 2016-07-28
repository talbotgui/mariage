package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Invite implements Serializable {
	private static final long serialVersionUID = 1L;

	private Age age;

	@ManyToMany
	@JoinTable(name = "PRESENCE", //
			joinColumns = { @JoinColumn(name = "INVITE_ID", insertable = false, updatable = false) }, //
			inverseJoinColumns = { @JoinColumn(name = "ETAPE_ID", insertable = false, updatable = false) }//
	)
	private Collection<Etape> etapesPresence = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "FOYER_ID")
	private Foyer foyer;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nom;

	private String prenom;

	protected Invite() {
		super();
	}

	public Invite(final Long id, final String nom, final String prenom, final Age age) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setAge(age);
	}

	public Invite(final Long id, final String nom, final String prenom, final Age age, final Foyer foyer) {
		this(id, nom, prenom, age);
		this.setFoyer(foyer);
	}

	public Invite(final String nom, final String prenom, final Age age) {
		this(null, nom, prenom, age);
	}

	public Age getAge() {
		return this.age;
	}

	public Collection<Etape> getEtapesPresence() {
		return new ArrayList<>(this.etapesPresence);
	}

	public Foyer getFoyer() {
		return this.foyer;
	}

	public Long getId() {
		return this.id;
	}

	public String getNom() {
		return this.nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setAge(final Age age) {
		this.age = age;
	}

	public void setEtapesPresence(final Collection<Etape> etapesPresence) {
		this.etapesPresence = new ArrayList<>(etapesPresence);
	}

	public void setFoyer(final Foyer foyer) {
		this.foyer = foyer;
	}

	private void setId(final Long id) {
		this.id = id;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

}
