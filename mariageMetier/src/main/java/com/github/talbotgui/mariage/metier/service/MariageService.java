package com.github.talbotgui.mariage.metier.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;

public interface MariageService {

	Mariage chargeMariageParId(Long idMariage);

	Collection<Etape> listeEtapesParIdMariage(Long idMariage);

	Collection<Invite> listeInvitesParIdMariage(Long idMariage);

	Page<Invite> listeInvitesParIdMariage(Long idMariage, Pageable page);

	Collection<Mariage> listeTousMariages();

	Long sauvegarde(Mariage mariage);

	Long sauvegardeGrappe(Mariage mariage);
}
