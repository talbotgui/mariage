package com.github.talbotgui.mariage.metier.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.talbotgui.mariage.metier.dao.CourrierRepository;
import com.github.talbotgui.mariage.metier.dao.EtapeRepository;
import com.github.talbotgui.mariage.metier.dao.InviteRepository;
import com.github.talbotgui.mariage.metier.dao.MariageRepository;
import com.github.talbotgui.mariage.metier.dao.PresenceEtapeRepository;
import com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesRepartitionsInvitesMariage;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.PresenceEtape;
import com.github.talbotgui.mariage.metier.exception.BusinessException;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MariageServiceImpl implements MariageService {

	@Autowired
	private CourrierRepository courrierRepository;

	@Autowired
	private EtapeRepository etapeRepository;

	@Autowired
	private InviteRepository inviteRepository;

	@Autowired
	private MariageRepository mariageRepository;

	@Autowired
	private PresenceEtapeRepository presenceEtapeRepository;

	private Map<String, Integer> arrayToMap(final List<Object[]> liste) {
		final Map<String, Integer> result = new HashMap<>();
		if (liste != null) {
			for (final Object[] objects : liste) {
				final String key = (objects[0] != null) ? objects[0].toString() : "";
				final int value = ((Number) objects[1]).intValue();
				result.put(key, value);
			}
		}
		return result;
	}

	@Override
	public StatistiquesMariage calculStatistiques(final Long idMariage) {
		final StatistiquesInvitesMariage invites = this.inviteRepository.calculStatistiquesInvites(idMariage);

		final Map<String, Integer> nbInvitesParGroupe = arrayToMap(
				this.inviteRepository.compteNombreInviteParGroupe(idMariage));
		final Map<String, Integer> nbInvitesParFoyer = arrayToMap(
				this.inviteRepository.compteNombreInviteParFoyer(idMariage));
		final Map<String, Integer> nbInvitesParAge = arrayToMap(
				this.inviteRepository.compteNombreInviteParAge(idMariage));
		final Map<String, Integer> nbInvitesParEtape = arrayToMap(
				this.inviteRepository.compteNombreInviteParEtape(idMariage));
		final Map<String, Integer> nbFoyersParEtape = arrayToMap(
				this.inviteRepository.compteNombreFoyerParEtape(idMariage));
		final StatistiquesRepartitionsInvitesMariage repartition = new StatistiquesRepartitionsInvitesMariage(
				nbInvitesParAge, nbInvitesParFoyer, nbInvitesParGroupe, nbInvitesParEtape, nbFoyersParEtape);
		return new StatistiquesMariage(repartition, invites);
	}

	@Override
	public Invite chargeInviteParId(final Long id) {
		return this.inviteRepository.findOne(id);
	}

	@Override
	public Mariage chargeMariageParId(final Long idMariage) {
		return this.mariageRepository.findOne(idMariage);
	}

	@Override
	public void lieUneEtapeEtUnCourrier(final Long idMariage, final Long idEtape, final Long idCourrier,
			final boolean lie) {
		final Courrier c = this.courrierRepository.findOne(idCourrier);
		final Etape e = this.etapeRepository.findOne(idEtape);

		if (c == null || e == null || !c.getMariage().getId().equals(idMariage)
				|| !e.getMariage().getId().equals(idMariage)) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}

		// Pour lier
		if (lie && !c.getEtapesInvitation().contains(e)) {
			c.addEtapeInvitation(e);
			this.courrierRepository.save(c);
		}

		// Pour délier
		if (!lie && c.getEtapesInvitation().contains(e)) {
			c.removeEtapeInvitatino(e);
			this.courrierRepository.save(c);
		}

	}

	@Override
	public Collection<String> listeAgePossible() {
		final Collection<String> liste = new ArrayList<>();
		for (final Age a : Age.values()) {
			liste.add(a.name());
		}
		return liste;
	}

	@Override
	public Collection<Courrier> listeCourriersParIdMariage(final Long idMariage) {
		return this.mariageRepository.listeCourriersParIdMariageFetchEtapesInvitation(idMariage);
	}

	@Override
	public Collection<Etape> listeEtapesParIdMariage(final Long idMariage) {
		return this.mariageRepository.listeEtapesParIdMariage(idMariage);
	}

	@Override
	public Collection<Invite> listeInvitesParIdMariage(final Long idMariage) {
		return inviteRepository.listeInvitesParIdMariage(idMariage);
	}

	@Override
	public Page<Invite> listeInvitesParIdMariage(final Long idMariage, final Pageable page) {
		return inviteRepository.listeInvitesParIdMariage(idMariage, page);
	}

	@Override
	public Collection<Mariage> listeTousMariages() {
		final Collection<Mariage> liste = new ArrayList<>();
		liste.addAll((Collection<Mariage>) this.mariageRepository.findAll());
		return liste;
	}

	@Override
	public void modifiePresenceEtape(final Long idMariage, final Long idPresenceEtape, final Boolean presence) {
		final PresenceEtape pe = this.presenceEtapeRepository.findPresenceEtape(idMariage, idPresenceEtape);
		pe.setPresent(presence);
	}

	@Override
	public Long sauvegarde(final Long idMariage, final Courrier courrier) {
		courrier.setMariage(this.mariageRepository.findOne(idMariage));
		return this.courrierRepository.save(courrier).getId();
	}

	@Override
	public Long sauvegarde(final Long idMariage, final Etape etape) {
		etape.setMariage(this.mariageRepository.findOne(idMariage));
		if (etape.getNumOrdre() == null) {
			etape.setNumOrdre(1 + this.etapeRepository.getNombreEtapeByIdMariage(idMariage));
		}
		final boolean estNouveau = etape.getId() == null;
		final Long idEtape = this.etapeRepository.save(etape).getId();
		if (estNouveau) {
			this.presenceEtapeRepository.insertPresenceEtapePourNouvelEtape(idEtape, idMariage);
		}
		return idEtape;
	}

	@Override
	public Long sauvegarde(final Long idMariage, final Invite invite) {

		// save
		invite.setMariage(this.mariageRepository.findOne(idMariage));
		final Long id = this.inviteRepository.save(invite).getId();

		// Set PresenceEtape
		this.presenceEtapeRepository.insertPresenceEtapePourNouvelInvite(id, idMariage);

		// mAj adresse et téléphone du foyer
		if (invite.getAdresse() != null) {
			this.inviteRepository.updateAdresseEtTelephoneEtEmailParFoyer(invite.getFoyer(), invite.getAdresse(),
					invite.getTelephone(), invite.getEmail());
		}

		return id;
	}

	@Override
	public Long sauvegarde(final Mariage m) {
		return this.mariageRepository.save(m).getId();
	}

	@Override
	public void sauvegardeEnMasse(final Long idMariage, final Collection<Invite> invites) {
		if (invites != null) {
			for (final Invite invite : invites) {
				this.sauvegarde(idMariage, invite);
			}
		}
	}

	@Override
	public Long sauvegardeGrappe(final Mariage m) {
		final Long id = this.sauvegarde(m);

		for (final Etape e : m.getEtapes()) {
			this.sauvegarde(id, e);
		}

		for (final Courrier c : m.getCourriers()) {
			this.sauvegarde(id, c);
		}

		for (final Invite i : m.getInvites()) {
			this.sauvegarde(id, i);
		}

		return id;
	}

	@Override
	public void supprimeCourrier(final Long idMariage, final Long idCourrier) {
		if (idMariage == null || !idMariage.equals(this.courrierRepository.getIdMariageByCourrierId(idCourrier))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}
		this.courrierRepository.delete(idCourrier);
	}

	@Override
	public void supprimeEtape(final Long idMariage, final Long idEtape) {
		if (idMariage == null || !idMariage.equals(this.etapeRepository.getIdMariageByEtapeId(idEtape))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}
		if (this.etapeRepository.countCourriersInvitant(idEtape) > 0) {
			throw new BusinessException(BusinessException.ERREUR_COURRIER_LIE_A_ETAPE, new Object[] {});
		}
		this.etapeRepository.delete(idEtape);
	}

	@Override
	public void supprimeInvite(final Long idMariage, final Long idInvite) {
		if (idMariage == null || !idMariage.equals(this.inviteRepository.getIdMariageByInviteId(idInvite))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}
		this.inviteRepository.delete(idInvite);
	}

	@Override
	public void supprimeMariage(final Long idMariage) {
		this.courrierRepository.delete(this.listeCourriersParIdMariage(idMariage));
		this.etapeRepository.delete(this.listeEtapesParIdMariage(idMariage));
		this.inviteRepository.delete(this.listeInvitesParIdMariage(idMariage));
		this.mariageRepository.delete(idMariage);
	}

}
