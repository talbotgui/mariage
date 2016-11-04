package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;
import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur.Role;

public interface SecuriteService {

	void ajouteAutorisation(String login, Long idMariage);

	void deverrouilleUtilisateur(final String login);

	Collection<String> listeRolePossible();

	Collection<Utilisateur> listeUtilisateurs();

	void sauvegardeUtilisateur(String login, String mdp, Utilisateur.Role role);

	void supprimeAutorisation(String login, Long idMariage);

	void supprimeUtilisateur(String login);

	Role verifieUtilisateur(String login, String mdp);
}
