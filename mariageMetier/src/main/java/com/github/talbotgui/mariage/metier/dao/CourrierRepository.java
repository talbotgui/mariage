package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Foyer;

@Transactional
public interface CourrierRepository extends CrudRepository<Courrier, Long> {

	@Query("delete Courrier where id in (select c.id from Courrier c where c.mariage.id = :idMariage)")
	@Modifying
	void deleteCourriersParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select distinct f from Courrier c"//
			+ " join c.foyersInvites f"//
			+ " left join fetch f.invites"//
			+ " where c.id=:idCourrier and c.mariage.id=:idMariage")
	Collection<Foyer> findFoyersInvitesByIdCourrierFetchInvites(@Param("idMariage") Long idMariage,
			@Param("idCourrier") Long idCourrier);

	@Query("select c.mariage.id from Courrier c where c.id=:idCourrier")
	Long getIdMariageByCourrierId(@Param("idCourrier") Long idCourrier);

}
