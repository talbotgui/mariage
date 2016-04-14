package com.github.talbotgui.mariage.metier.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.PresenceEtape;

public interface PresenceEtapeRepository extends CrudRepository<PresenceEtape, Long> {

	@Query("select pe from PresenceEtape pe where pe.invite.mariage.id = :idMariage and pe.id = :idPresenceEtape")
	PresenceEtape findPresenceEtape(@Param("idMariage") Long idMariage, @Param("idPresenceEtape") Long idPresenceEtape);

	@Modifying
	@Query("insert into PresenceEtape (etape, invite) select e, i from Invite i join i.mariage m join m.etapes e where e.id = :idEtape and m.id = :idMariage")
	void insertPresenceEtapePourNouvelEtape(@Param("idEtape") Long idEtape, @Param("idMariage") Long idMariage);

	@Modifying
	@Query("insert into PresenceEtape (etape, invite) select e, i from Etape e join e.mariage m join m.invites i where i.id = :idInvite and m.id = :idMariage and e.id not in (select pe.etape.id from PresenceEtape pe where pe.invite.id = :idInvite)")
	void insertPresenceEtapePourNouvelInvite(@Param("idInvite") Long idInvite, @Param("idMariage") Long idMariage);

}
