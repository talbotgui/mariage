package com.github.talbotgui.mariage.metier.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Invitation;

@Transactional
public interface InvitationRepository extends CrudRepository<Invitation, Long> {

	@Query("select fei from Invitation fei"//
			+ " where fei.id.foyer.mariage.id = :idMariage"//
			+ " and fei.id.foyer.id = :idFoyer"//
			+ " and fei.id.courrier.id = :idCourrier")
	Invitation rechercherInvitation(@Param("idMariage") Long idMariage, @Param("idCourrier") Long ididCourrier,
			@Param("idFoyer") Long idFoyer);

	@Query("delete Invitation where id in"//
			+ " (select fei.id from Invitation fei where fei.id.foyer.mariage.id = :idMariage)")
	@Modifying
	void supprimerInvitationParIdMariage(@Param("idMariage") Long idMariage);

	@Query("delete Invitation where id in"//
			+ " (select fei.id from Invitation fei where fei.id.courrier.id = :idCourrier)")
	@Modifying
	void supprimerInvitationsParIdCourrier(@Param("idCourrier") Long idCourrier);

	@Query("delete Invitation where id in"//
			+ " (select fei.id from Invitation fei where fei.id.foyer.id = :idFoyer)")
	@Modifying
	void supprimerInvitationsParIdFoyer(@Param("idFoyer") Long idFoyer);
}
