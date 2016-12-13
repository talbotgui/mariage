package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;

public interface SecuriteService {

	Long ajouterAutorisation(String login, Long idMariage);

	Utilisateur chargerUtilisateur(String login);

	void deverrouillerUtilisateur(final String login);

	Collection<Autorisation> listerAutorisations();

	Collection<String> listerRolePossible();

	Collection<Utilisateur> listerUtilisateurs();

	Collection<Utilisateur> listerUtilisateursParMariage(Long idMariage);

	/**
	 * A la creation d'un utilisateur, si aucun role fournit, le nouvel
	 * utilisateur sera un UTILISATEUR.
	 *
	 * @param login
	 *            Identifiant unique
	 * @param mdp
	 *            Mot de passe
	 * @param role
	 *            Role dans l'application
	 */
	void sauvegarderUtilisateur(String login, String mdp, Utilisateur.Role role);

	void supprimerAutorisation(Long idAutorisation);

	void supprimerUtilisateur(String login);

	Role verifierUtilisateur(String login, String mdp);
}
