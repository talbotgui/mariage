package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage;
import com.github.talbotgui.mariage.metier.entities.Invite;

@Transactional(readOnly = true)
public interface InviteRepository extends PagingAndSortingRepository<Invite, Long> {

	@Query("select new com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage("//
			//
			+ " (select count(distinct i.foyer.nom) from Mariage m"//
			+ "            join m.foyers f join f.invites i"//
			+ "            where m.id = :idMariage)"//
			+ ", (select count(distinct i.foyer.groupe) from Mariage m"//
			+ "            join m.foyers f join f.invites i"//
			+ "            where m.id = :idMariage)"//
			+ ", (select size(i) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage)"//
			+ ", (select size(i) from Mariage m"//
			+ "            join m.foyers f join f.invites i"//
			+ "            where m.id = :idMariage and ("//
			+ "               i.nom is null or i.prenom is null or i.foyer.groupe is null or i.foyer.nom is null))"//
			+ ", (select size(i) from Mariage m"//
			+ "            join m.foyers f join f.invites i"//
			+ "            where m.id = :idMariage and i.foyer.adresse is null)"//
			+ ", (select size(i) from Mariage m"//
			+ "            join m.foyers f join f.invites i"//
			+ "            where m.id = :idMariage and i.age is null)"//
			+ ")"//
			+ " from Mariage m"//
			+ " where m.id = :idMariage")
	StatistiquesInvitesMariage calculerStatistiquesInvites(@Param("idMariage") Long idMariage);

	@Query("select e.nom, count(distinct f)"//
			+ " from Foyer f join f.courriersInvitation c join c.etapes e"//
			+ " where f.mariage.id = :idMariage group by e.nom")
	List<Object[]> compterNombreFoyerParEtape(@Param("idMariage") Long idMariage);

	@Query("select i.age, count(distinct i)"//
			+ " from Mariage m join m.foyers f join f.invites i"//
			+ " where m.id = :idMariage group by i.age")
	List<Object[]> compterNombreInviteParAge(@Param("idMariage") Long idMariage);

	@Query("select e.nom, count(distinct i)"//
			+ " from Invitation inv join inv.id.courrier c join c.etapes e join c.mariage m"//
			+ " join inv.id.foyer f join f.invites i"//
			+ " where m.id = :idMariage group by e.nom")
	List<Object[]> compterNombreInviteParEtape(@Param("idMariage") Long idMariage);

	@Query("select f.nom, size(f.invites) from Mariage m join m.foyers f where m.id = :idMariage group by f.nom")
	List<Object[]> compterNombreInviteParFoyer(@Param("idMariage") Long idMariage);

	@Query("select f.groupe, count(distinct i)"//
			+ " from Mariage m join m.foyers f join f.invites i"//
			+ " where m.id = :idMariage group by f.groupe")
	List<Object[]> compterNombreInviteParGroupe(@Param("idMariage") Long idMariage);

	@Query("select distinct i" //
			+ " from Invite i join fetch i.foyer f left join fetch i.etapesPresence"//
			+ " where i.foyer.mariage.id=:idMariage"//
			+ " order by f.groupe, f.nom, i.nom, i.prenom")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Invite> listerInvitesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select i "//
			+ " from Invite i"//
			+ " where i.foyer.mariage.id=:idMariage")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Page<Invite> listerInvitesParIdMariage(@Param("idMariage") Long idMariage, Pageable page);

	@Query("select i "//
			+ " from Presence p"//
			+ " join p.id.invite i"//
			+ " where i.foyer.mariage.id=:idMariage"//
			+ " and p.present = true"//
			+ " order by i.foyer.groupe, i.nom, i.prenom")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Invite> listerInvitesPresentsParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select i.foyer.mariage.id from Invite i where i.id=:idInvite")
	Long rechercherIdMariageByInviteId(@Param("idInvite") Long idInvite);

	@Query("select i.id, i.nom, i.prenom, e.nom, count(i.id)"//
			+ " from Invitation inv"//
			+ " join inv.id.courrier c"//
			+ " join c.etapes e"//
			+ " join inv.id.foyer f"//
			+ " join f.invites i"//
			+ " where i.foyer.mariage.id=:idMariage"//
			+ " group by i.id, i.nom, i.prenom, e.nom"//
			+ " having count(i.id) >1")
	Collection<Object[]> rechercherInviteSurPlusieursEtapes(@Param("idMariage") Long idMariage);

	@Query("delete Invite where id in (select i.id from Invite i where i.foyer.mariage.id = :idMariage)")
	@Modifying
	void supprimerInvitesParIdMariage(@Param("idMariage") Long idMariage);

}
