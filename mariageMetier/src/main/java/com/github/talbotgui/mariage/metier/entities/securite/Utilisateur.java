package com.github.talbotgui.mariage.metier.entities.securite;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Utilisateur implements Serializable {
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

	private Date premierEchec;
	private Date secondEchec;
	private Date troisiemeEchec;

	public Utilisateur() {
		super();
	}

	public Utilisateur(final String login, final String mdp) {
		super();
		this.setLogin(login);
		this.setMdp(mdp);
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
		return this.premierEchec;
	}

	public Date getSecondEchec() {
		return this.secondEchec;
	}

	public Date getTroisiemeEchec() {
		return this.troisiemeEchec;
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
		this.premierEchec = premierEchec;
	}

	public void setSecondEchec(final Date secondEchec) {
		this.secondEchec = secondEchec;
	}

	public void setTroisiemeEchec(final Date troisiemeEchec) {
		this.troisiemeEchec = troisiemeEchec;
	}

	@Override
	public String toString() {
		return "Utilisateur [login=" + this.login + ", mdp=" + this.mdp + "]";
	}

}
