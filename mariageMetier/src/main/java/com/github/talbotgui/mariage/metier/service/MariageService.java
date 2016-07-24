package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;

public interface MariageService {

	StatistiquesMariage calculStatistiques(Long idMariage);

	Foyer chargeFoyerParId(Long id);

	Invite chargeInviteParId(Long id);

	Mariage chargeMariageParId(Long idMariage);

	Foyer getFoyer(Long idMariage, String nomFoyer);

	void lieUneEtapeEtUnCourrier(Long idMariage, Long idEtape, Long idCourrier, boolean lie);

	Collection<String> listeAgePossible();

	Collection<Courrier> listeCourriersParIdMariage(Long idMariage);

	Collection<Etape> listeEtapesParIdMariage(Long idMariage);

	Collection<Foyer> listeFoyersParIdMariage(Long idMariage);

	Collection<Invite> listeInvitesParIdMariage(Long idMariage);

	Page<Invite> listeInvitesParIdMariage(Long idMariage, Pageable page);

	Collection<Mariage> listeTousMariages();

	void modifieFoyerEtapeInvitation(final Long idMariage, final Long idEtape, final Long idFoyer,
			final boolean invitation);

	Long sauvegarde(Long idMariage, Courrier courrier);

	Long sauvegarde(Long idMariage, Etape etape);

	Long sauvegarde(Long idMariage, Foyer foyer);

	Long sauvegarde(Mariage mariage);

	void sauvegardeEnMasse(Long idMariage, Collection<Invite> invites);

	Long sauvegardeGrappe(Mariage mariage);

	Long sauvegardeInviteEtFoyer(Long idMariage, Invite invite);

	void supprimeCourrier(Long idMariage, Long idCourrier);

	void supprimeEtape(Long idMariage, Long idEtape);

	void supprimeInvite(Long idMariage, Long idInvite);

	void supprimeMariage(Long idMariage);
}
