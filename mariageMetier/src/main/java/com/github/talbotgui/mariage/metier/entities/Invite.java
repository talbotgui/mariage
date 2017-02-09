package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
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
public class Invite implements Serializable {
	private static final long serialVersionUID = 1L;

	private Age age;

	private String commentaire;

	@ManyToMany(cascade = CascadeType.REMOVE)
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

	@Column(name = "PARTICIPANT_AUX_ANIMATIONS")
	private Boolean participantAuxAnimations;

	private Boolean particularite;

	private String prenom;

	protected Invite() {
		super();
	}

	public Invite(final Long idInvite) {
		this.id = idInvite;
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

	public String getCommentaire() {
		return this.commentaire;
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

	public Boolean getParticipantAuxAnimations() {
		return this.participantAuxAnimations;
	}

	public Boolean getParticularite() {
		return this.particularite;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public final void setAge(final Age age) {
		this.age = age;
	}

	public final void setCommentaire(final String commentaire) {
		this.commentaire = commentaire;
	}

	public final void setEtapesPresence(final Collection<Etape> etapesPresence) {
		this.etapesPresence = new ArrayList<>(etapesPresence);
	}

	public final void setFoyer(final Foyer foyer) {
		this.foyer = foyer;
	}

	private final void setId(final Long id) {
		this.id = id;
	}

	public final void setNom(final String nom) {
		this.nom = nom;
	}

	public final void setParticipantAuxAnimations(final Boolean participantAuxAnimations) {
		this.participantAuxAnimations = participantAuxAnimations;
	}

	public final void setParticularite(final Boolean particularite) {
		this.particularite = particularite;
	}

	public final void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

}
