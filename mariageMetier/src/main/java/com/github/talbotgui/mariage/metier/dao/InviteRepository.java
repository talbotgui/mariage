package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Invite;

public interface InviteRepository extends PagingAndSortingRepository<Invite, Long> {

	void deleteInviteByIdAndMariageId(Long id, Long mariageId);

	@Query("select distinct i from Invite i left join fetch i.presencesEtape where i.mariage.id=:idMariage order by i.groupe, i.nom")
	Collection<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select i from Invite i where i.mariage.id=:idMariage order by i.groupe, i.nom")
	Page<Invite> listeInvitesParIdMariage(@Param("idMariage") Long idMariage, Pageable page);
}
