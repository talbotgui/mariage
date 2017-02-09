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
import javax.persistence.JoinTable;
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
	@JoinTable(name = "EVENEMENT_PARTICIPANTS", joinColumns = {
			@JoinColumn(name = "EVENEMENT") }, inverseJoinColumns = { @JoinColumn(name = "PARTICIPANTS") })
	private Collection<Utilisateur> participants = new ArrayList<>();

	private String titre;

	protected Evenement() {
		super();
	}

	public Evenement(final Long id) {
		super();
		this.setId(id);
	}

	public Evenement(final Long id, final String titre, final Date debut, final Date fin,
			final Collection<Utilisateur> participants) {
		super();
		this.setId(id);
		this.setTitre(titre);
		this.setDebut(debut);
		this.setFin(fin);
		this.setParticipants(participants);
	}

	public Evenement(final String titre, final Date debut, final Date fin) {
		this(titre, debut, fin, null);
	}

	public Evenement(final String titre, final Date debut, final Date fin, final Collection<Utilisateur> participants) {
		this(null, titre, debut, fin, participants);
	}

	public Date getDebut() {
		if (this.debut != null) {
			return new Date(this.debut.getTime());
		}
		return null;
	}

	public Date getFin() {
		if (this.fin != null) {
			return new Date(this.fin.getTime());
		}
		return null;
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

	public final void setDebut(final Date debut) {
		if (debut != null) {
			this.debut = new Date(debut.getTime());
		}
	}

	public final void setFin(final Date fin) {
		if (fin != null) {
			this.fin = new Date(fin.getTime());
		}
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public final void setParticipants(final Collection<Utilisateur> participants) {
		this.participants = participants;
	}

	public final void setTitre(final String titre) {
		this.titre = titre;
	}

}
