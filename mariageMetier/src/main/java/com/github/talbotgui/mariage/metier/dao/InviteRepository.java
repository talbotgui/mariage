package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage;
import com.github.talbotgui.mariage.metier.entities.Invite;

@Transactional(readOnly = true)
public interface InviteRepository extends PagingAndSortingRepository<Invite, Long> {

	@Query("select new com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage("//
			//
			+ " (select count(distinct i.foyer.nom) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage)"
			+ ", (select count(distinct i.foyer.groupe) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage)"
			+ ", (select size(i) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage)"
			+ ", (select size(i) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage and"
			+ " (i.nom is null or i.prenom is null or i.foyer.groupe is null or i.foyer.nom is null))"
			+ ", (select size(i) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage and i.foyer.adresse is null)"
			+ ", (select size(i) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage and i.age is null)"//
			+ ")"//
			+ " from Mariage m"//
			+ " where m.id = :idMariage")
	StatistiquesInvitesMariage calculStatistiquesInvites(@Param("idMariage") Long idMariage);

	@Query("select e.nom, count(distinct inv)"//
			+ " from Invitation inv join inv.id.courrier c join c.etapes e join c.mariage m "//
			+ " where m.id = :idMariage group by e.nom")
	List<Object[]> compteNombreFoyerParEtape(@Param("idMariage") Long idMariage);

	@Query("select i.age, count(distinct i)"//
			+ " from Mariage m join m.foyers f join f.invites i"//
			+ " where m.id = :idMariage group by i.age")
	List<Object[]> compteNombreInviteParAge(@Param("idMariage") Long idMariage);

	@Query("select e.nom, count(distinct i)"//
			+ " from Invitation inv join inv.id.courrier c join c.etapes e join c.mariage m"//
			+ " join inv.id.foyer f join f.invites i"//
			+ " where m.id = :idMariage group by e.nom")
	List<Object[]> compteNombreInviteParEtape(@Param("idMariage") Long idMariage);

	@Query("select f.nom, size(f.invites) from Mariage m join m.foyers f where m.id = :idMariage group by f.nom")
	List<Object[]> compteNombreInviteParFoyer(@Param("idMariage") Long idMariage);

	@Query("select f.groupe, count(distinct i) from Mariage m join m.foyers f join f.invites i where m.id = :idMariage group by f.groupe")
	List<Object[]> compteNombreInviteParGroupe(@Param("idMariage") Long idMariage);

	@Query("delete Invite where id in (select i.id from Invite i where i.foyer.mariage.id = :idMariage)")
	@Modifying
	void deleteInvitesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select i.foyer.mariage.id from Invite i where i.id=:idInvite")
	Long getIdMariageByInviteId(@Param("idInvite") Long idInvite);

	@Query("select distinct i" //
			+ " from Invite i join fetch i.foyer f left join fetch i.etapesPresence"//
			+ " where i.foyer.mariage.id=:idMariage"//
			+ " order by f.groupe, f.nom, i.nom, i.prenom")
	Collection<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select i "//
			+ " from Invite i"//
			+ " where i.foyer.mariage.id=:idMariage"//
			+ " order by i.foyer.groupe, i.nom")
	Page<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage, Pageable page);

}
