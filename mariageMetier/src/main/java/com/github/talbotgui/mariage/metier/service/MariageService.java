package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Evenement;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.Presence;

public interface MariageService {

	StatistiquesMariage calculerStatistiques(Long idMariage);

	Collection<StatistiquesPresenceMariage> calculerStatistiquesPresence(Long idMariage, Long idEtape);

	Foyer chargerFoyerParId(Long id);

	Invite chargerInviteParId(Long id);

	Mariage chargerMariageParId(Long idMariage);

	Presence chargerPresenceParEtapeEtInvite(Long idMariage, Long idEtape, Long idInvite);

	String genererPublipostage(Long idMariage, Long idCourrier);

	Foyer chargerFoyer(Long idMariage, String nomFoyer);

	void lierUneEtapeEtUnCourrier(Long idMariage, Long idEtape, Long idCourrier, boolean lie);

	void lierUnFoyerEtUnCourrier(final Long idMariage, final Long idCourrier, final Long idFoyer,
			final boolean invitation);

	Collection<String> listerAgePossible();

	Collection<Courrier> listerCourriersParIdMariage(Long idMariage);

	Collection<Etape> listerEtapesParIdMariage(Long idMariage);

	Collection<Evenement> listerEvenementsParIdMariage(Long idMariage);

	Collection<Foyer> listerFoyersParIdCourrier(Long idMariage, Long idCourrier);

	Collection<Foyer> listerFoyersParIdMariage(Long idMariage);

	Collection<Invite> listerInvitesParIdMariage(Long idMariage);

	Page<Invite> listerInvitesParIdMariage(Long idMariage, Pageable page);

	Collection<Invite> listerInvitesPresentsParIdMariage(Long idMariage);

	Collection<Mariage> listerMariagesAutorises(String login);

	Collection<Presence> listerPresencesParIdMariage(Long idMariage);

	Collection<Mariage> listerTousMariages();

	Collection<String> rechercherErreurs(Long idMariage);

	void sauvegarderEnMasse(Long idMariage, Collection<Invite> invites);

	Long sauvegarder(Long idMariage, Courrier courrier);

	Long sauvegarder(Long idMariage, Etape etape);

	Long sauvegarder(Long idMariage, Evenement evenement);

	Long sauvegarder(Long idMariage, Foyer foyer);

	void sauvegarder(Long idMariage, Presence presence);

	Long sauvegarder(Mariage mariage);

	Long sauvegarderGrappe(Mariage mariage);

	Long sauvegarderInviteEtFoyer(Long idMariage, Invite invite);

	void supprimerCourrier(Long idMariage, Long idCourrier);

	void supprimerEtape(Long idMariage, Long idEtape);

	void supprimerEvenement(Long idMariage, Long idEvenement);

	void supprimerInvite(Long idMariage, Long idInvite);

	void supprimerMariage(Long idMariage);

	void supprimerPresence(Long idMariage, Long idInvite, Long idEtape);
}
