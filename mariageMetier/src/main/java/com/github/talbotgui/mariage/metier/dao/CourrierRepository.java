package com.github.talbotgui.mariage.metier.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Courrier;

public interface CourrierRepository extends CrudRepository<Courrier, Long> {

	@Query("select c.mariage.id from Courrier c where c.id=:idCourrier")
	Long getIdMariageByCourrierId(@Param("idCourrier") Long idCourrier);

}
