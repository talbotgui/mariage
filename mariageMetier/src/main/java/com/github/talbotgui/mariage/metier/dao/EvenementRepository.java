package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Evenement;

@Transactional
public interface EvenementRepository extends CrudRepository<Evenement, Long> {

	@Query("select e from Evenement e where e.mariage.id = :idMariage")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Evenement> listeEvenementsParIdMariageId(@Param("idMariage") Long idMariage);

	@Query("select e.mariage.id from Evenement e where e.id=:idEvenement")
	Long rechercherIdMariageByEvenementId(@Param("idEvenement") Long idEvenement);
}
