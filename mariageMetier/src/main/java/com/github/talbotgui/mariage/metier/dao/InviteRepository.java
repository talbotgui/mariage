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
			+ " (select count(distinct i.foyer) from Mariage m join m.invites i where m.id = :idMariage)"
			+ ", (select count(distinct i.groupe) from Mariage m join m.invites i where m.id = :idMariage)"
			+ ", (select size(i) from Mariage m join m.invites i where m.id = :idMariage)"
			+ ", (select size(i) from Mariage m join m.invites i where m.id = :idMariage and"
			+ " (i.nom is null or i.prenom is null or i.groupe is null or i.foyer is null))"
			+ ", (select size(i) from Mariage m join m.invites i where m.id = :idMariage and i.adresse is null)"
			+ ", (select size(i) from Mariage m join m.invites i where m.id = :idMariage and i.age is null)"//
			+ ")"//
			+ " from Mariage m"//
			+ " where m.id = :idMariage")
	StatistiquesInvitesMariage calculStatistiquesInvites(@Param("idMariage") Long idMariage);

	@Query("select i.age, count(i) from Mariage m join m.invites i where m.id = :idMariage group by i.age")
	List<Object[]> compteNombreInviteParAge(@Param("idMariage") Long idMariage);

	@Query("select pe.etape.nom, count(i)"//
			+ " from Mariage m join m.invites i join i.presencesEtape pe"//
			+ " where m.id = :idMariage and pe.present = true group by pe.etape.nom")
	List<Object[]> compteNombreInviteParEtape(@Param("idMariage") Long idMariage);

	@Query("select i.foyer, count(i) from Mariage m join m.invites i where m.id = :idMariage group by i.foyer")
	List<Object[]> compteNombreInviteParFoyer(@Param("idMariage") Long idMariage);

	@Query("select i.groupe, count(i) from Mariage m join m.invites i where m.id = :idMariage group by i.groupe")
	List<Object[]> compteNombreInviteParGroupe(@Param("idMariage") Long idMariage);

	@Query("select i.mariage.id from Invite i where i.id=:idInvite")
	Long getIdMariageByInviteId(@Param("idInvite") Long idInvite);

	@Query("select distinct i" //
			+ " from Invite i left join fetch i.presencesEtape "//
			+ " where i.mariage.id=:idMariage"//
			+ " order by i.groupe, i.foyer, i.nom, i.prenom")
	Collection<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select i "//
			+ " from Invite i"//
			+ " where i.mariage.id=:idMariage"//
			+ " order by i.groupe, i.nom")
	Page<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage, Pageable page);

	@Transactional(readOnly = false)
	@Modifying
	@Query("update Invite"//
			+ " set adresse=:adresse, telephone=:telephone, email=:email"//
			+ " where foyer=:foyer")
	void updateAdresseEtTelephoneEtEmailParFoyer(@Param("foyer") String foyer, @Param("adresse") String adresse,
			@Param("telephone") String telephone, @Param("email") String email);
}
