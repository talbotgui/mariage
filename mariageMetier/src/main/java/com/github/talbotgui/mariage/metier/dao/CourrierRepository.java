package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Foyer;

@Transactional
public interface CourrierRepository extends CrudRepository<Courrier, Long> {

	@Query("select distinct f from Courrier c"//
			+ " join c.foyersInvites f"//
			+ " left join fetch f.invites"//
			+ " where c.id=:idCourrier and c.mariage.id=:idMariage")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Foyer> rechercherFoyersInvitesByIdCourrierFetchInvites(@Param("idMariage") Long idMariage,
			@Param("idCourrier") Long idCourrier);

	@Query("select c.mariage.id from Courrier c where c.id=:idCourrier")
	Long rechercherIdMariageByCourrierId(@Param("idCourrier") Long idCourrier);

	@Query("delete Courrier where id in (select c.id from Courrier c where c.mariage.id = :idMariage)")
	@Modifying
	void supprimerCourriersParIdMariage(@Param("idMariage") Long idMariage);
}
