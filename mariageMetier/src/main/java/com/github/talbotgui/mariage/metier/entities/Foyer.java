package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

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

	public void addCourrierInvitation(final Courrier c1) {
		this.courriersInvitation.add(c1);
	}

	public void addInvite(final Invite i) {
		this.invites.add(i);
	}

	/**
	 * Genere une ligne de texte contenant, séparés par un ';', :
	 * <ul>
	 * <li>La concatenation des noms des invites</li>
	 * <li>Le nom de la rue</li>
	 * <li>le code postal et la ville</li>
	 * </ul>
	 *
	 * @return
	 */
	public String genereLignePublipostage() {
		final StringBuilder sb = new StringBuilder();

		// Tri des invites par nom puis age
		final Comparator<Invite> parNom = (i1, i2) -> i1.getNom().compareTo(i2.getNom()) * 10000
				+ i1.getAge().compareTo(i2.getAge()) * 100 + i1.getPrenom().compareTo(i2.getPrenom());
		final Set<Invite> invitesTries = new TreeSet<>(parNom);
		invitesTries.addAll(this.invites);

		// noms
		String nomPrecedent = null;
		for (final Invite i : invitesTries) {
			if (nomPrecedent == null) {
				sb.append(i.getNom());
			} else if (!nomPrecedent.equals(i.getNom())) {
				sb.append(" et ");
				sb.append(i.getNom());
			} else {
				sb.append(",");
			}
			sb.append(" ");
			sb.append(i.getPrenom());
			nomPrecedent = i.getNom();
		}
		sb.append(";");

		// rueEtVille
		if (this.adresse != null) {
			sb.append(this.adresse.replaceFirst("[ ]*[\\-_]+[ ]*", ";"));
		} else {
			sb.append(";");
		}
		return sb.toString();
	}

	public String getAdresse() {
		return this.adresse;
	}

	public Collection<Courrier> getCourriersInvitation() {
		return new ArrayList<>(this.courriersInvitation);
	}

	public String getEmail() {
		return this.email;
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

	public void setCourriersInvitation(final Collection<Courrier> courriersInvitation) {
		this.courriersInvitation = new ArrayList<>(courriersInvitation);
	}

	public void setEmail(final String email) {
		this.email = email;
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
