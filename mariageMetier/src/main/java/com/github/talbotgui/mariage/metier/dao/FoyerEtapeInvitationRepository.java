package com.github.talbotgui.mariage.metier.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.FoyerEtapeInvitation;

public interface FoyerEtapeInvitationRepository extends CrudRepository<FoyerEtapeInvitation, Long> {

	@Query("delete FoyerEtapeInvitation where id in (select fei.id from FoyerEtapeInvitation fei where fei.id.foyer.mariage.id = :idMariage)")
	@Modifying
	void deletePresenceEtapesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select fei from FoyerEtapeInvitation fei"//
			+ " where fei.id.foyer.mariage.id = :idMariage and fei.id.foyer.id = :idFoyer and fei.id.etape.id = :idEtape")
	FoyerEtapeInvitation findFoyerEtapeInvitation(@Param("idMariage") Long idMariage, @Param("idEtape") Long idEtape,
			@Param("idFoyer") Long idFoyer);
}
