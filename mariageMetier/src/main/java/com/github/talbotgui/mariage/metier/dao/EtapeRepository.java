package com.github.talbotgui.mariage.metier.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Etape;

public interface EtapeRepository extends CrudRepository<Etape, Long> {

	@Query("select e.mariage.id from Etape e where e.id=:idEtape order by numOrdre")
	Long getIdMariageByEtapeId(@Param("idEtape") Long idEtape);

	@Query("select size(m.etapes) from Mariage m where m.id = :idMariage")
	Integer getNombreEtapeByIdMariage(@Param("idMariage") Long idMariage);

}
