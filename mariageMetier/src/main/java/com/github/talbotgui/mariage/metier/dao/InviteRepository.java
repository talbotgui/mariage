package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Invite;

public interface InviteRepository extends PagingAndSortingRepository<Invite, Long> {

	@Query("select i.mariage.id from Invite i where i.id=:idInvite")
	Long getIdMariageByInviteId(@Param("idInvite") Long idInvite);

	@Query("select distinct i from Invite i left join fetch i.presencesEtape where i.mariage.id=:idMariage order by i.groupe, i.nom")
	Collection<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select i from Invite i where i.mariage.id=:idMariage order by i.groupe, i.nom")
	Page<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage, Pageable page);

	@Modifying
	@Query("update Invite set adresse=:adresse, telephone=:telephone where foyer=:foyer")
	void updateAdresseEtTelephoneParFoyer(@Param("foyer") String foyer, @Param("adresse") String adresse,
			@Param("telephone") String telephone);
}
