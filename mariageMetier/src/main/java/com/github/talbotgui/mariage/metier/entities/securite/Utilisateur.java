package com.github.talbotgui.mariage.metier.entities.securite;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Utilisateur implements Serializable {

	public enum Role {
		ADMIN, MARIE, UTILISATEUR
	}

	private static final Logger LOG = LoggerFactory.getLogger(Utilisateur.class);

	/**
	 * Nombre de jours après lesquels une erreur de connexion est ignorée et
	 * annulée.
	 */
	private static final long NB_JOUR_DELAI_EFFACEMENT_ECHEC = 3;

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.REMOVE })
	private Collection<Autorisation> autorisations;

	@Id
	private String login;

	private String mdp;

	@Column(name = "PREMIER_ECHEC")
	private Date premierEchec;

	@Enumerated(EnumType.STRING)
	private Role role = Role.UTILISATEUR;

	@Column(name = "SECOND_ECHEC")
	private Date secondEchec;

	@Column(name = "TROISIEME_ECHEC")
	private Date troisiemeEchec;

	public Utilisateur() {
		super();
	}

	public Utilisateur(final String login) {
		super();
		this.setLogin(login);
	}

	public Utilisateur(final String login, final String mdp) {
		super();
		this.setLogin(login);
		this.setMdp(mdp);
	}

	public Utilisateur(final String login, final String mdp, final Role role) {
		super();
		this.setLogin(login);
		this.setMdp(mdp);
		this.setRole(role);
	}

	public void declarerEchecDeConnexion() {
		this.rangeLesEchecs();

		if (this.premierEchec == null) {
			this.premierEchec = new Date();
		} else if (this.secondEchec == null) {
			this.secondEchec = new Date();
		} else if (this.troisiemeEchec == null) {
			this.troisiemeEchec = new Date();
		} else {
			LOG.warn("Tentative répétées de connexion de l'utilisateur {}", this.login);
		}
	}

	public void deverrouilleUtilisateur() {
		this.premierEchec = null;
		this.secondEchec = null;
		this.troisiemeEchec = null;
	}

	public Collection<Autorisation> getAutorisations() {
		return this.autorisations;
	}

	public String getLogin() {
		return this.login;
	}

	public String getMdp() {
		return this.mdp;
	}

	public Date getPremierEchec() {
		if (this.premierEchec != null) {
			return new Date(this.premierEchec.getTime());
		}
		return null;
	}

	public Role getRole() {
		return this.role;
	}

	public Date getSecondEchec() {
		if (this.secondEchec != null) {
			return new Date(this.secondEchec.getTime());
		}
		return null;
	}

	public Date getTroisiemeEchec() {
		if (this.troisiemeEchec != null) {
			return new Date(this.troisiemeEchec.getTime());
		}
		return null;
	}

	public boolean isVerrouille() {
		return this.premierEchec != null && this.secondEchec != null && this.troisiemeEchec != null;
	}

	private void rangeLesEchecs() {

		// Calcul de la date avant laquelle un echec de connexion est oublié
		final Date dateLimite = Date.from(Instant.now().minus(NB_JOUR_DELAI_EFFACEMENT_ECHEC, ChronoUnit.DAYS));

		// Annulation des échecs de connexion
		for (int i = 0; i < 3; i++) {
			if (this.premierEchec != null && this.premierEchec.before(dateLimite)) {
				this.premierEchec = this.secondEchec;
				this.secondEchec = this.troisiemeEchec;
			}
		}

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

	public void setPremierEchec(final Date premierEchec) {
		if (premierEchec != null) {
			this.premierEchec = new Date(premierEchec.getTime());
		}
	}

	public void setRole(final Role role) {
		if (role != null) {
			this.role = role;
		}
	}

	public void setSecondEchec(final Date secondEchec) {
		if (secondEchec != null) {
			this.secondEchec = new Date(secondEchec.getTime());
		}
	}

	public void setTroisiemeEchec(final Date troisiemeEchec) {
		if (troisiemeEchec != null) {
			this.troisiemeEchec = new Date(troisiemeEchec.getTime());
		}
	}

	@Override
	public String toString() {
		return "Utilisateur [login=" + this.login + ", mdp=" + this.mdp + "]";
	}

}
