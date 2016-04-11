package com.github.talbotgui.mariage.metier.dao.securite;

import org.springframework.data.repository.CrudRepository;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, String> {

}
