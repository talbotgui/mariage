package com.github.talbotgui.mariage.metier.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.talbotgui.mariage.metier.dao.MariageRepository;
import com.github.talbotgui.mariage.metier.dao.securite.AutorisationRepository;
import com.github.talbotgui.mariage.metier.dao.securite.UtilisateurRepository;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
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
	private UtilisateurRepository utilisateurRepo;

	@Override
	public void ajouteAutorisation(final String login, final Long idMariage) {
		final Mariage m = this.mariageRepo.findOne(idMariage);
		final Utilisateur u = this.utilisateurRepo.findOne(login);
		this.autorisationRepo.save(new Autorisation(m, u));
	}

	@Override
	public void creeUtilisateur(final String login, final String mdp) {
		if (login == null || login.length() < LOGIN_MDP_MIN || mdp == null || mdp.length() < LOGIN_MDP_MIN) {
			throw new BusinessException(BusinessException.ERREUR_LOGIN_MDP, new Object[] { LOGIN_MDP_MIN });
		}
		this.utilisateurRepo.save(new Utilisateur(login, encrypt(mdp)));
	}

	private String encrypt(final String mdp) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(mdp.getBytes("UTF-8"));
			return new String(md.digest(), "UTF-8");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new BusinessException(BusinessException.ERREUR_SHA);
		}
	}

	@Override
	public void supprimeAutorisation(final String login, final Long idMariage) {
		this.utilisateurRepo.delete(login);
	}

	@Override
	public void supprimeUtilisateur(final String login) {
		this.utilisateurRepo.delete(login);
	}

	@Override
	public void verifieUtilisateur(final String login, final String mdp) {
		final Utilisateur u = this.utilisateurRepo.findOne(login);
		if (u == null || !u.getMdp().equals(encrypt(mdp))) {
			throw new BusinessException(BusinessException.ERREUR_LOGIN);
		}
	}

}
