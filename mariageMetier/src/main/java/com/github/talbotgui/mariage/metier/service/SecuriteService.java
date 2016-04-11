package com.github.talbotgui.mariage.metier.service;

public interface SecuriteService {

	void ajouteAutorisation(String login, Long idMariage);

	void creeUtilisateur(String login, String mdp);

	void supprimeAutorisation(String login, Long idMariage);

	void supprimeUtilisateur(String login);

	void verifieUtilisateur(String login, String mdp);
}
