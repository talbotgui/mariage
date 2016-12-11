package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

@Entity
public class Evenement implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date debut;
	private Date fin;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "MARIAGE_ID")
	private Mariage mariage;

	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Utilisateur> participants = new ArrayList<>();

	private String titre;

	protected Evenement() {
		super();
	}

	public Evenement(final Long id) {
		super();
		this.id = id;
	}

	public Evenement(final Long id, final String titre, final Date debut, final Date fin,
			final Collection<Utilisateur> participants) {
		super();
		this.id = id;
		this.titre = titre;
		this.debut = debut;
		this.fin = fin;
		this.participants = participants;
	}

	public Evenement(final String titre, final Date debut, final Date fin) {
		this(titre, debut, fin, null);
	}

	public Evenement(final String titre, final Date debut, final Date fin, final Collection<Utilisateur> participants) {
		this(null, titre, debut, fin, participants);
	}

	public Date getDebut() {
		return this.debut;
	}

	public Date getFin() {
		return this.fin;
	}

	public Long getId() {
		return this.id;
	}

	public Mariage getMariage() {
		return this.mariage;
	}

	public Collection<Utilisateur> getParticipants() {
		return this.participants;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setDebut(final Date debut) {
		this.debut = debut;
	}

	public void setFin(final Date fin) {
		this.fin = fin;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public void setParticipants(final Collection<Utilisateur> participants) {
		this.participants = participants;
	}

	public void setTitre(final String titre) {
		this.titre = titre;
	}

}
