package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Invite implements Serializable {
	private static final long serialVersionUID = 1L;

	private Age age;

	private String groupe;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "MARIAGE_ID")
	private Mariage mariage;

	private String nom;

	private String prenom;

	@OneToMany
	@JoinColumn(name = "INVITE_ID")
	private Collection<PresenceEtape> presencesEtape = new ArrayList<>();

	protected Invite() {
		super();
	}

	public Invite(final Long id, final String groupe, final String nom, final String prenom, final Age age) {
		super();
		this.groupe = groupe;
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
	}

	public Invite(final String groupe, final String nom, final String prenom, final Age age) {
		super();
		this.groupe = groupe;
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
	}

	public Invite(final String groupe, final String nom, final String prenom, final Age age,
			final PresenceEtape... presences) {
		this(groupe, nom, prenom, age);
		this.presencesEtape.addAll(Arrays.asList(presences));
	}

	public Age getAge() {
		return age;
	}

	public String getGroupe() {
		return groupe;
	}

	public Long getId() {
		return id;
	}

	public Mariage getMariage() {
		return mariage;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public Collection<PresenceEtape> getPresencesEtape() {
		return presencesEtape;
	}

	public void setAge(final Age age) {
		this.age = age;
	}

	public void setGroupe(final String groupe) {
		this.groupe = groupe;
	}

	public void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

	public void setPresencesEtape(final Collection<PresenceEtape> presencesEtape) {
		this.presencesEtape = presencesEtape;
	}

}
