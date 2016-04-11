package com.github.talbotgui.mariage.metier.entities.securite;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.github.talbotgui.mariage.metier.entities.Mariage;

@Entity
public class Autorisation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ID_MARIAGE")
	private Mariage mariage;

	@ManyToOne
	@JoinColumn(name = "ID_UTILISATEUR")
	private Utilisateur utilisateur;

	public Autorisation() {
		super();
	}

	public Autorisation(final Mariage mariage, final Utilisateur utilisateur) {
		super();
		this.mariage = mariage;
		this.utilisateur = utilisateur;
	}

	public Long getId() {
		return id;
	}

	public Mariage getMariage() {
		return mariage;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setMariage(final Mariage mariage) {
		this.mariage = mariage;
	}

	public void setUtilisateur(final Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
}
