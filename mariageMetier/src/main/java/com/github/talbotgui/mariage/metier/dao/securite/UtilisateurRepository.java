package com.github.talbotgui.mariage.metier.dao.securite;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, String> {

	@Query("select u from Utilisateur u order by u.login")
	Collection<Utilisateur> listerUtilisateur();

}
