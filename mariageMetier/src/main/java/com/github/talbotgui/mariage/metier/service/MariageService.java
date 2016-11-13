package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.Presence;

public interface MariageService {

	StatistiquesMariage calculStatistiques(Long idMariage);

	Collection<StatistiquesPresenceMariage> calculStatistiquesPresence(Long idMariage);

	Foyer chargeFoyerParId(Long id);

	Invite chargeInviteParId(Long id);

	Mariage chargeMariageParId(Long idMariage);

	Presence chargePresenceParEtapeEtInvite(Long idMariage, Long idEtape, Long idInvite);

	String generePublipostage(Long idMariage, Long idCourrier);

	Foyer getFoyer(Long idMariage, String nomFoyer);

	void lieUneEtapeEtUnCourrier(Long idMariage, Long idEtape, Long idCourrier, boolean lie);

	void lieUnFoyerEtUnCourrier(final Long idMariage, final Long idEtape, final Long idFoyer, final boolean invitation);

	Collection<String> listeAgePossible();

	Collection<Courrier> listeCourriersParIdMariage(Long idMariage);

	Collection<Etape> listeEtapesParIdMariage(Long idMariage);

	Collection<Foyer> listeFoyersParIdCourrier(Long idMariage, Long idCourrier);

	Collection<Foyer> listeFoyersParIdMariage(Long idMariage);

	Collection<Invite> listeInvitesParIdMariage(Long idMariage);

	Page<Invite> listeInvitesParIdMariage(Long idMariage, Pageable page);

	Collection<Presence> listePresencesParIdMariage(Long idMariage);

	Collection<Mariage> listeTousMariages();

	Long sauvegarde(Long idMariage, Courrier courrier);

	Long sauvegarde(Long idMariage, Etape etape);

	Long sauvegarde(Long idMariage, Foyer foyer);

	void sauvegarde(Long idMariage, Presence presence);

	Long sauvegarde(Mariage mariage);

	void sauvegardeEnMasse(Long idMariage, Collection<Invite> invites);

	Long sauvegardeGrappe(Mariage mariage);

	Long sauvegardeInviteEtFoyer(Long idMariage, Invite invite);

	void supprimeCourrier(Long idMariage, Long idCourrier);

	void supprimeEtape(Long idMariage, Long idEtape);

	void supprimeInvite(Long idMariage, Long idInvite);

	void supprimeMariage(Long idMariage);

	void supprimePresence(Long idMariage, Long idInvite, Long idEtape);
}
