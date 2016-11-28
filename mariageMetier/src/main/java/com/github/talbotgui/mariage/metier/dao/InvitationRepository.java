package com.github.talbotgui.mariage.metier.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Invitation;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

	@Query("delete Invitation where id in"//
			+ " (select fei.id from Invitation fei where fei.id.foyer.mariage.id = :idMariage)")
	@Modifying
	void deleteInvitationParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select fei from Invitation fei"//
			+ " where fei.id.foyer.mariage.id = :idMariage"//
			+ " and fei.id.foyer.id = :idFoyer"//
			+ " and fei.id.courrier.id = :idCourrier")
	Invitation findInvitation(@Param("idMariage") Long idMariage, @Param("idCourrier") Long ididCourrier,
			@Param("idFoyer") Long idFoyer);

	@Query("delete Invitation where id in"//
			+ " (select fei.id from Invitation fei where fei.id.courrier.id = :idCourrier)")
	@Modifying
	void supprimeInvitationsParIdCourrier(@Param("idCourrier") Long idCourrier);

	@Query("delete Invitation where id in"//
			+ " (select fei.id from Invitation fei where fei.id.foyer.id = :idFoyer)")
	@Modifying
	void supprimeInvitationsParIdFoyer(@Param("idFoyer") Long idFoyer);
}
