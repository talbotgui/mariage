package com.github.talbotgui.mariage.metier.entities.securite;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Utilisateur {

	@OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.REMOVE })
	private Collection<Autorisation> autorisations;

	@Id
	private String login;

	private String mdp;

	public Utilisateur() {
		super();
	}

	public Utilisateur(final String login, final String mdp) {
		super();
		this.login = login;
		this.mdp = mdp;
	}

	public Collection<Autorisation> getAutorisations() {
		return autorisations;
	}

	public String getLogin() {
		return login;
	}

	public String getMdp() {
		return mdp;
	}

	public void setAutorisations(final Collection<Autorisation> autorisations) {
		this.autorisations = autorisations;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public void setMdp(final String mdp) {
		this.mdp = mdp;
	}

	@Override
	public String toString() {
		return "Utilisateur [login=" + login + ", mdp=" + mdp + "]";
	}

}
