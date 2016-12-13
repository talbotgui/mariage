package com.github.talbotgui.mariage.metier.dao.securite;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.securite.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, String> {

	@Query("select u from Utilisateur u order by u.login")
	Collection<Utilisateur> listerUtilisateur();

	@Query("select u from Mariage m join m.autorisations a join a.utilisateur u where m.id = :idMariage order by u.login")
	Collection<Utilisateur> listerUtilisateursParMariage(@Param("idMariage") Long idMariage);

}
