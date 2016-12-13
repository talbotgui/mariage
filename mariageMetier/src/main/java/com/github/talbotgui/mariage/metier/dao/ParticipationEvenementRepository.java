package com.github.talbotgui.mariage.metier.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.ParticipationEvenement;
import com.github.talbotgui.mariage.metier.entities.ParticipationEvenementId;

@Transactional
public interface ParticipationEvenementRepository
		extends CrudRepository<ParticipationEvenement, ParticipationEvenementId> {

	@Query("delete ParticipationEvenement where id.participant.login = :login")
	@Modifying
	void supprimerParticipationParLoginParticipant(@Param("login") String login);
}
