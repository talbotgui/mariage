package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
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

	private String adresse;

	private Age age;

	private String foyer;

	private String groupe;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "MARIAGE_ID")
	private Mariage mariage;

	private String nom;

	private String prenom;

	@OneToMany(mappedBy = "invite", cascade = { CascadeType.REMOVE })
	private Collection<PresenceEtape> presencesEtape = new ArrayList<>();

	private String telephone;

	protected Invite() {
		super();
	}

	public Invite(final Long id, final String groupe, final String nom, final String prenom, final Age age) {
		this(groupe, nom, prenom, age);
		this.setId(id);
	}

	public Invite(final String groupe, final String nom, final String prenom, final Age age) {
		super();
		this.setGroupe(groupe);
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setAge(age);
	}

	public Invite(final String groupe, final String foyer, final String nom, final String prenom, final Age age,
			final PresenceEtape... presences) {
		this(groupe, nom, prenom, age);
		this.setPresencesEtape(Arrays.asList(presences));
		this.setFoyer(foyer);
	}

	public String getAdresse() {
		return adresse;
	}

	public Age getAge() {
		return age;
	}

	public String getFoyer() {
		return foyer;
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
		final List<PresenceEtape> result = new ArrayList<>();
		result.addAll(presencesEtape);
		result.sort(new Comparator<PresenceEtape>() {
			@Override
			public int compare(final PresenceEtape o1, final PresenceEtape o2) {
				return o1.getEtape().getNumOrdre() - o2.getEtape().getNumOrdre();
			}
		});
		return result;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setAdresse(final String adresse) {
		this.adresse = adresse;
	}

	public void setAge(final Age age) {
		this.age = age;
	}

	public void setFoyer(final String foyer) {
		this.foyer = foyer;
	}

	public void setGroupe(final String groupe) {
		this.groupe = groupe;
	}

	private void setId(final Long id) {
		this.id = id;
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
		this.presencesEtape = new ArrayList<>(presencesEtape);
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

}
