package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage;
import com.github.talbotgui.mariage.metier.entities.Presence;

@Transactional
public interface PresenceRepository extends CrudRepository<Presence, Long> {
	@Query("select new com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage("//
			+ "e.id, e.nom,"//
			+ " (select count(p) from Presence p where p.id.etape.id=e.id and p.present=false),"//
			+ " (select count(p) from Presence p where p.id.etape.id=e.id and p.present=false and p.confirmee = true),"//
			+ " (select count(p) from Presence p where p.id.etape.id=e.id and p.present=true),"//
			+ " (select count(p) from Presence p where p.id.etape.id=e.id and p.present=true and p.confirmee = true),"//
			+ " (select count(i) from Courrier c join c.etapes eta join c.foyersInvites f join f.invites i where eta.id=e.id))"//
			+ " from Etape e"//
			+ " where e.mariage.id=:idMariage" //
			+ " order by e.numOrdre")
	Collection<StatistiquesPresenceMariage> calculStatistiquesPresence(@Param("idMariage") Long idMariage);

	@Query("select p "//
			+ " from Presence p"//
			+ " where p.id.invite.id = :idInvite"//
			+ " and p.id.etape.id=:idEtape"//
			+ " and p.id.etape.mariage.id=:idMariage")
	Presence chargePresenceParEtapeEtInvite(@Param("idMariage") Long idMariage, @Param("idEtape") Long idEtape,
			@Param("idInvite") Long idInvite);

	@Query("select p "//
			+ " from Presence p"//
			+ " join fetch p.id.etape e"//
			+ " join fetch p.id.invite i" //
			+ " where p.id.etape.mariage.id=:idMariage"//
			+ " order by p.id.invite.nom, p.id.invite.prenom, p.id.etape.nom")
	Collection<Presence> listePresencesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select new Presence(e, i) "//
			+ " from Invite i join i.foyer f join f.courriersInvitation c join c.etapes e"//
			+ " where f.mariage.id=:idMariage"//
			+ " and e.mariage.id=:idMariage" //
			+ " order by i.nom, i.prenom, e.nom")
	Collection<Presence> listeProduitCartesienInviteEtEtapeParIdMariage(@Param("idMariage") Long idMariage);
}
