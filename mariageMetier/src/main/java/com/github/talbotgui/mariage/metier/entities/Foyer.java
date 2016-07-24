package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Foyer implements Serializable {
	private static final long serialVersionUID = 1L;

	private String adresse;

	@ManyToMany
	@JoinTable(name = "FOYER_COURRIER_INVITATION", //
			joinColumns = { @JoinColumn(name = "FOYER_ID", insertable = false, updatable = false) }, //
			inverseJoinColumns = { @JoinColumn(name = "COURRIER_ID", insertable = false, updatable = false) }//
	)
	private Collection<Courrier> courriersInvitation = new ArrayList<>();

	private String email;

	private String groupe;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy = "foyer", cascade = { CascadeType.REMOVE })
	private Collection<Invite> invites = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "MARIAGE_ID")
	private Mariage mariage;

	private String nom;

	private String telephone;

	protected Foyer() {
		super();
	}

	public Foyer(final Long id) {
		super();
		this.id = id;
	}

	public Foyer(final Long id, final String groupe, final String nom, final String adresse, final String email,
			final String telephone) {
		super();
		this.setId(id);
		this.setGroupe(groupe);
		this.setNom(nom);
		this.setAdresse(adresse);
		this.setEmail(email);
		this.setTelephone(telephone);
	}

	public Foyer(final String nom) {
		super();
		this.nom = nom;
	}

	public Foyer(final String groupe, final String foyer) {
		super();
		this.groupe = groupe;
		this.nom = foyer;
	}

	public Foyer(final String groupe, final String nom, final String adresse, final String email,
			final String telephone) {
		this(null, groupe, nom, adresse, email, telephone);
	}

	public void addInvite(final Invite i) {
		this.invites.add(i);
	}

	public String getAdresse() {
		return this.adresse;
	}

	public String getEmail() {
		return this.email;
	}

	public Collection<Courrier> getCourriersInvitation() {
		return new ArrayList<>(this.courriersInvitation);
	}

	public String getGroupe() {
		return this.groupe;
	}

	public Long getId() {
		return this.id;
	}

	public Collection<Invite> getInvites() {
		return new ArrayList<>(this.invites);
	}

	public Mariage getMariage() {
		return this.mariage;
	}

	public String getNom() {
		return this.nom;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setAdresse(final String adresse) {
		this.adresse = adresse;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setCourriersInvitation(final Collection<Courrier> courriersInvitation) {
		this.courriersInvitation = new ArrayList<>(courriersInvitation);
	}

	public void setGroupe(final String groupe) {
		this.groupe = groupe;
	}

	private void setId(final Long id) {
		this.id = id;
	}

	public void setInvites(final Collection<Invite> invites) {
		this.invites = new ArrayList<>(invites);
	}

	public void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

}
