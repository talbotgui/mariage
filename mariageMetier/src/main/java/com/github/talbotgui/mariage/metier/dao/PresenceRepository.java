package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage;
import com.github.talbotgui.mariage.metier.entities.Presence;

@Transactional
public interface PresenceRepository extends CrudRepository<Presence, Long> {
	@Query("select distinct new com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage("//
			+ "e.id, i.age,"//
			+ " (select count(p) from Presence p where p.id.invite.age=i.age and p.id.etape.id=e.id "//
			+ "     and p.present=false),"//
			+ " (select count(p) from Presence p where p.id.invite.age=i.age and p.id.etape.id=e.id "//
			+ "      and p.present=false and p.confirmee=true),"//
			+ " (select count(p) from Presence p where p.id.invite.age=i.age and p.id.etape.id=e.id "//
			+ "      and p.present=true),"//
			+ " (select count(p) from Presence p where p.id.invite.age=i.age and p.id.etape.id=e.id "//
			+ "      and p.present=true and p.confirmee=true),"//
			+ " (select count(inv) from Courrier c join c.etapes eta join c.foyersInvites f" //
			+ "      join f.invites inv where eta.id=e.id and inv.age = i.age)"//
			+ " )"//
			+ " from Etape e, Invite i"//
			+ " where e.mariage.id = :idMariage" //
			+ " and e.id = :idEtape")
	Collection<StatistiquesPresenceMariage> calculerStatistiquesPresence(@Param("idMariage") Long idMariage,
			@Param("idEtape") Long idEtape);

	@Query("select p "//
			+ " from Presence p"//
			+ " where p.id.invite.id = :idInvite"//
			+ " and p.id.etape.id=:idEtape"//
			+ " and p.id.etape.mariage.id=:idMariage")
	Presence chargerPresenceParEtapeEtInvite(@Param("idMariage") Long idMariage, @Param("idEtape") Long idEtape,
			@Param("idInvite") Long idInvite);

	@Query("delete Presence where id in (select p.id from Presence p where p.id.etape.mariage.id = :idMariage)")
	@Modifying
	void supprimerPresencesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select p "//
			+ " from Presence p"//
			+ " join fetch p.id.etape e"//
			+ " join fetch p.id.invite i" //
			+ " where p.id.etape.mariage.id=:idMariage"//
			+ " order by p.id.invite.nom, p.id.invite.prenom, p.id.etape.nom")
	Collection<Presence> listerPresencesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select new Presence(e, i) "//
			+ " from Invite i join i.foyer f join f.courriersInvitation c join c.etapes e"//
			+ " where f.mariage.id=:idMariage"//
			+ " and e.mariage.id=:idMariage" //
			+ " order by i.nom, i.prenom, e.nom")
	Collection<Presence> listerProduitCartesienInviteEtEtapeParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select p.id.invite.id, p.id.invite.nom, p.id.invite.prenom, p.id.etape.nom from Presence p"//
			+ " where p.id.etape.mariage.id = :idMariage"//
			+ " and concat(str(p.id.invite.foyer.id), '-', str(p.id.etape.id)) not in ("//
			+ " select concat(str(i.id.foyer.id), '-', str(e.id)) "//
			+ " from Invitation i join i.id.courrier c join c.etapes e"//
			+ " where e.mariage.id = :idMariage"//
			+ ")")
	Collection<Object[]> rechercherPresencesSansInvitations(@Param("idMariage") Long idMariage);

	@Query("delete Presence where id in (select p.id from Presence p where p.id.etape.id = :idEtape)")
	@Modifying
	void supprimerPresencesParIdEtape(@Param("idEtape") Long idEtape);
}
