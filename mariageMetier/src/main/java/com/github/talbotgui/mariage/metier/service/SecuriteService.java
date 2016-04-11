package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public interface SecuriteService {

	void ajouteAutorisation(String login, Long idMariage);

	void creeUtilisateur(String login, String mdp);

	Collection<Utilisateur> listeUtilisateurs();

	void supprimeAutorisation(String login, Long idMariage);

	void supprimeUtilisateur(String login);

	void verifieUtilisateur(String login, String mdp);
}
