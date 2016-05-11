package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;

public interface MariageService {

	Mariage chargeMariageParId(Long idMariage);

	Collection<String> listeAgePossible();

	Collection<Courrier> listeCourriersParIdMariage(Long idMariage);

	Collection<Etape> listeEtapesParIdMariage(Long idMariage);

	Collection<Invite> listeInvitesParIdMariage(Long idMariage);

	Page<Invite> listeInvitesParIdMariage(Long idMariage, Pageable page);

	Collection<Mariage> listeTousMariages();

	void modifiePresenceEtape(Long idMariage, Long idPresenceEtape, Boolean presence);

	Long sauvegarde(Long idMariage, Courrier courrier);

	Long sauvegarde(Long idMariage, Etape etape);

	Long sauvegarde(Long idMariage, Invite invite);

	Long sauvegarde(Mariage mariage);

	Long sauvegardeGrappe(Mariage mariage);

	void suprimeCourrier(Long idMariage, Long idCourrier);

	void suprimeEtape(Long idMariage, Long idEtape);

	void suprimeInvite(Long idMariage, Long idInvite);
}
