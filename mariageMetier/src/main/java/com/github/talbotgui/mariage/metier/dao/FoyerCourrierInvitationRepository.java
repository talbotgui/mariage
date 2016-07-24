package com.github.talbotgui.mariage.metier.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.FoyerCourrierInvitation;

public interface FoyerCourrierInvitationRepository extends CrudRepository<FoyerCourrierInvitation, Long> {

	@Query("delete FoyerCourrierInvitation where id in (select fei.id from FoyerCourrierInvitation fei where fei.id.foyer.mariage.id = :idMariage)")
	@Modifying
	void deleteFoyerCourrierInvitationParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select fei from FoyerCourrierInvitation fei"//
			+ " where fei.id.foyer.mariage.id = :idMariage and fei.id.foyer.id = :idFoyer and fei.id.courrier.id = :idCourrier")
	FoyerCourrierInvitation findFoyerCourrierInvitation(@Param("idMariage") Long idMariage,
			@Param("idCourrier") Long ididCourrier, @Param("idFoyer") Long idFoyer);
}
