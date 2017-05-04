package com.github.talbotgui.mariage.metier.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.talbotgui.mariage.metier.dao.MariageRepository;
import com.github.talbotgui.mariage.metier.dao.ParticipationEvenementRepository;
import com.github.talbotgui.mariage.metier.dao.securite.AutorisationRepository;
import com.github.talbotgui.mariage.metier.dao.securite.UtilisateurRepository;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;
import com.github.talbotgui.mariage.metier.exception.BusinessException;

@Service
public class SecuriteServiceImpl implements SecuriteService {
	/** Longueur minimale des logins et mots de passe. */
	private static final Long LOGIN_MDP_MIN = 6L;

	@Autowired
	private AutorisationRepository autorisationRepo;

	@Autowired
	private MariageRepository mariageRepo;

	@Autowired
	private ParticipationEvenementRepository participationEvenementRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepo;

	@Override
	public Long ajouterAutorisation(final String login, final Long idMariage) {
		final Mariage m = this.mariageRepo.findOne(idMariage);
		final Utilisateur u = this.utilisateurRepo.findOne(login);
		return this.autorisationRepo.save(new Autorisation(m, u)).getId();
	}

	@Override
	public Utilisateur chargerUtilisateur(final String login) {
		return this.utilisateurRepo.findOne(login);
	}

	@Override
	public void deverrouillerUtilisateur(final String login) {
		final Utilisateur u = this.utilisateurRepo.findOne(login);
		u.deverrouilleUtilisateur();
		this.utilisateurRepo.save(u);
	}

	private String encrypt(final String mdp) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(mdp.getBytes("UTF-8"));
			return new String(md.digest(), "UTF-8");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new BusinessException(BusinessException.ERREUR_SHA, e);
		}
	}

	@Override
	public Collection<Autorisation> listerAutorisations() {
		return this.autorisationRepo.listerAutorisations();
	}

	@Override
	public Collection<String> listerRolePossible() {
		final Collection<String> liste = new ArrayList<>();
		for (final Role r : Role.values()) {
			liste.add(r.name());
		}
		return liste;
	}

	@Override
	public Collection<Utilisateur> listerUtilisateurs() {
		return this.utilisateurRepo.listerUtilisateur();
	}

	@Override
	public Collection<Utilisateur> listerUtilisateursParMariage(final Long idMariage) {
		return this.utilisateurRepo.listerUtilisateursParMariage(idMariage);
	}

	@Override
	public void resetPassword(final String login) {
		final Utilisateur u = this.utilisateurRepo.findOne(login);
		u.setMdp(this.encrypt(login));
		this.utilisateurRepo.save(u);
	}

	@Override
	public void sauvegarderUtilisateur(final String login, final String mdp, Utilisateur.Role role) {

		this.valideLoginOuMotDePasse(login);

		// Recherche
		final Utilisateur u = this.utilisateurRepo.findOne(login);

		// Creation
		if (u == null) {

			this.valideLoginOuMotDePasse(mdp);

			if (role == null) {
				role = Utilisateur.Role.UTILISATEUR;
			}
			this.utilisateurRepo.save(new Utilisateur(login, this.encrypt(mdp), role));
		}

		// MaJ
		else {
			if (role != null) {
				u.setRole(role);
			}
			if (mdp != null) {
				this.valideLoginOuMotDePasse(mdp);
				u.setMdp(this.encrypt(mdp));
			}
			this.utilisateurRepo.save(u);
		}
	}

	@Override
	public void supprimerAutorisation(final Long idAutorisation) {
		this.autorisationRepo.delete(idAutorisation);
	}

	@Override
	public void supprimerUtilisateur(final String login) {
		this.participationEvenementRepository.supprimerParticipationParLoginParticipant(login);
		this.utilisateurRepo.delete(login);
	}

	private void valideLoginOuMotDePasse(final String loginOuMdp) {
		if (loginOuMdp == null || loginOuMdp.length() < LOGIN_MDP_MIN) {
			throw new BusinessException(BusinessException.ERREUR_LOGIN_MDP, new Object[] { LOGIN_MDP_MIN });
		}
	}

	@Override
	public Role verifierUtilisateur(final String login, final String mdp) {

		final Utilisateur u = this.utilisateurRepo.findOne(login);

		// Si pas d'utilisateur correspondant
		if (u == null) {
			throw new BusinessException(BusinessException.ERREUR_LOGIN);
		}

		// Si l'utilisateur est verrouilé
		else if (u.isVerrouille()) {
			throw new BusinessException(BusinessException.ERREUR_LOGIN_VEROUILLE);
		}

		// Si erreur dans le mot de passe
		else if (!u.getMdp().equals(this.encrypt(mdp))) {

			// Init des dates d'echec et sauvegarde des modifications
			u.declarerEchecDeConnexion();
			this.utilisateurRepo.save(u);

			// renvoi de l'erreur
			throw new BusinessException(BusinessException.ERREUR_LOGIN);
		}

		// Si tout se passe bien, suppression des erreurs de connexion
		else {
			u.deverrouilleUtilisateur();
			this.utilisateurRepo.save(u);
			return u.getRole();
		}
	}

}
