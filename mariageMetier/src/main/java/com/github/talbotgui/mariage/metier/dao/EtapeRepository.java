package com.github.talbotgui.mariage.metier.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Etape;

@Transactional
public interface EtapeRepository extends CrudRepository<Etape, Long> {

	@Query("select count(c) from Courrier c join c.etapes e where e.id = :idEtape")
	int compterCourriersInvitant(@Param("idEtape") Long idEtape);

	@Query("delete Etape where id in (select e.id from Etape e where e.mariage.id = :idMariage)")
	@Modifying
	void supprimerEtapesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select e.mariage.id from Etape e where e.id=:idEtape")
	Long rechercherIdMariageByEtapeId(@Param("idEtape") Long idEtape);

	@Query("select size(m.etapes) from Mariage m where m.id = :idMariage")
	Integer compterEtapeByIdMariage(@Param("idMariage") Long idMariage);

}
