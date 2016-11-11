package com.github.talbotgui.mariage.metier.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.talbotgui.mariage.metier.dao.CourrierRepository;
import com.github.talbotgui.mariage.metier.dao.EtapeRepository;
import com.github.talbotgui.mariage.metier.dao.FoyerRepository;
import com.github.talbotgui.mariage.metier.dao.InvitationRepository;
import com.github.talbotgui.mariage.metier.dao.InviteRepository;
import com.github.talbotgui.mariage.metier.dao.MariageRepository;
import com.github.talbotgui.mariage.metier.dao.PresenceRepository;
import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.dto.StatistiquesInvitesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesMariage;
import com.github.talbotgui.mariage.metier.dto.StatistiquesRepartitionsInvitesMariage;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invitation;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.Presence;
import com.github.talbotgui.mariage.metier.exception.BusinessException;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MariageServiceImpl implements MariageService {

	@Autowired
	private CourrierRepository courrierRepository;

	@Autowired
	private EtapeRepository etapeRepository;

	@Autowired
	private FoyerRepository foyerRepository;

	@Autowired
	private InvitationRepository invitationRepository;

	@Autowired
	private InviteRepository inviteRepository;

	@Autowired
	private MariageRepository mariageRepository;

	@Autowired
	private PresenceRepository presenceRepository;

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

		final Map<String, Integer> nbInvitesParGroupe = this
				.arrayToMap(this.inviteRepository.compteNombreInviteParGroupe(idMariage));
		final Map<String, Integer> nbInvitesParFoyer = this
				.arrayToMap(this.inviteRepository.compteNombreInviteParFoyer(idMariage));
		final Map<String, Integer> nbInvitesParAge = this
				.arrayToMap(this.inviteRepository.compteNombreInviteParAge(idMariage));
		final Map<String, Integer> nbInvitesParEtape = this
				.arrayToMap(this.inviteRepository.compteNombreInviteParEtape(idMariage));
		final Map<String, Integer> nbFoyersParEtape = this
				.arrayToMap(this.inviteRepository.compteNombreFoyerParEtape(idMariage));
		final StatistiquesRepartitionsInvitesMariage repartition = new StatistiquesRepartitionsInvitesMariage(
				nbInvitesParAge, nbInvitesParFoyer, nbInvitesParGroupe, nbInvitesParEtape, nbFoyersParEtape);
		return new StatistiquesMariage(repartition, invites);
	}

	@Override
	public Foyer chargeFoyerParId(final Long id) {
		return this.foyerRepository.findOne(id);
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
	public Presence chargePresenceParEtapeEtInvite(final Long idMariage, final Long idEtape, final Long idInvite) {
		return this.presenceRepository.chargePresenceParEtapeEtInvite(idMariage, idEtape, idInvite);
	}

	@Override
	public String generePublipostage(final Long idMariage, final Long idCourrier) {
		final StringBuilder contenu = new StringBuilder("NOM;RUE;VILLE");

		final Collection<Foyer> foyersInvite = this.courrierRepository
				.findFoyersInvitesByIdCourrierFetchInvites(idMariage, idCourrier);

		for (final Foyer f : foyersInvite) {
			contenu.append("\n");
			contenu.append(f.genereLignePublipostage());
		}

		return contenu.toString();
	}

	@Override
	public Foyer getFoyer(final Long idMariage, final String nomFoyer) {
		return this.foyerRepository.findOneParNom(idMariage, nomFoyer);
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
		if (lie && !c.getEtapes().contains(e)) {
			c.addEtape(e);
			this.courrierRepository.save(c);
		}

		// Pour délier
		if (!lie && c.getEtapes().contains(e)) {
			c.removeEtape(e);
			this.courrierRepository.save(c);
		}

	}

	@Override
	public void lieUnFoyerEtUnCourrier(final Long idMariage, final Long idCourrier, final Long idFoyer,
			final boolean invitation) {
		final Invitation fei = this.invitationRepository.findInvitation(idMariage, idCourrier, idFoyer);
		if (fei != null && !invitation) {
			this.invitationRepository.delete(fei);
		} else if (fei == null && invitation) {
			this.invitationRepository.save(new Invitation(new Courrier(idCourrier), new Foyer(idFoyer)));
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
		return this.mariageRepository.listeCourriersParIdMariageFetchEtapesDuCourrier(idMariage);
	}

	@Override
	public Collection<Etape> listeEtapesParIdMariage(final Long idMariage) {
		return this.mariageRepository.listeEtapesParIdMariage(idMariage);
	}

	@Override
	public Collection<Foyer> listeFoyersParIdCourrier(final Long idMariage, final Long idCourrier) {
		return this.foyerRepository.listeFoyersParIdCourrier(idMariage, idCourrier);
	}

	@Override
	public Collection<Foyer> listeFoyersParIdMariage(final Long idMariage) {
		return this.foyerRepository.listeFoyersParIdMariage(idMariage);
	}

	@Override
	public Collection<Invite> listeInvitesParIdMariage(final Long idMariage) {
		return this.inviteRepository.listeInvitesParIdMariage(idMariage);
	}

	@Override
	public Page<Invite> listeInvitesParIdMariage(final Long idMariage, final Pageable page) {
		return this.inviteRepository.listeInvitesParIdMariage(idMariage, page);
	}

	@Override
	public Collection<Presence> listePresencesParIdMariage(final Long idMariage) {
		final Set<Presence> presenceFiltrees = new HashSet<>();

		// Ajout des presences existantes
		presenceFiltrees.addAll(this.presenceRepository.listePresencesParIdMariage(idMariage));

		// Ajout de toutes les autres presences possibles avec le produit
		// cartesien (celles déjà présentes restent et pas de doublon avec
		// Presence.hashcode)
		presenceFiltrees.addAll(this.presenceRepository.listeProduitCartesienInviteEtEtapeParIdMariage(idMariage));

		return presenceFiltrees;
	}

	@Override
	public Collection<Mariage> listeTousMariages() {
		final Collection<Mariage> liste = new ArrayList<>();
		liste.addAll((Collection<Mariage>) this.mariageRepository.findAll());
		return liste;
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
		return this.etapeRepository.save(etape).getId();
	}

	@Override
	public Long sauvegarde(final Long idMariage, final Foyer foyer) {
		foyer.setMariage(this.mariageRepository.findOne(idMariage));
		return this.foyerRepository.save(foyer).getId();

	}

	@Override
	public void sauvegarde(final Long idMariage, final Presence presence) {
		presence.setDateMaj(new Date());
		this.presenceRepository.save(presence);
	}

	@Override
	public Long sauvegarde(final Mariage m) {
		return this.mariageRepository.save(m).getId();
	}

	@Override
	public void sauvegardeEnMasse(final Long idMariage, final Collection<Invite> invites) {
		if (invites != null) {
			for (final Invite invite : invites) {
				this.sauvegardeInviteEtFoyer(idMariage, invite);
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

		for (final Foyer f : m.getFoyers()) {
			for (final Invite i : f.getInvites()) {
				this.sauvegardeInviteEtFoyer(id, i);
			}
			for (final Courrier c : f.getCourriersInvitation()) {
				this.lieUnFoyerEtUnCourrier(id, c.getId(), f.getId(), true);
			}
		}

		return id;
	}

	@Override
	public Long sauvegardeInviteEtFoyer(final Long idMariage, final Invite invite) {

		if (invite.getFoyer().getId() != null) {
			final Foyer f = this.foyerRepository.findOne(invite.getFoyer().getId());
			DTOUtils.copyBeanProperties(invite.getFoyer(), f, true, "adresse", "email", "groupe", "nom", "telephone");
			invite.setFoyer(f);
		}
		this.sauvegarde(idMariage, invite.getFoyer());

		return this.inviteRepository.save(invite).getId();
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

		// Verification de la coherence des ID
		if (idMariage == null || !idMariage.equals(this.inviteRepository.getIdMariageByInviteId(idInvite))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}

		// Suppression du foyer ?
		final Foyer f = this.inviteRepository.findOne(idInvite).getFoyer();
		final boolean supprimeFoyer = f.getInvites().size() == 1;

		// Supprime l'invite
		this.inviteRepository.delete(idInvite);

		// Supprime eventuellement le foyer
		if (supprimeFoyer) {
			this.foyerRepository.delete(f);
		}
	}

	@Override
	public void supprimeMariage(final Long idMariage) {
		this.courrierRepository.deleteCourriersParIdMariage(idMariage);
		this.invitationRepository.deleteInvitationParIdMariage(idMariage);
		this.etapeRepository.deleteEtapesParIdMariage(idMariage);
		this.inviteRepository.deleteInvitesParIdMariage(idMariage);
		this.foyerRepository.deleteFoyersParIdMariage(idMariage);
		this.mariageRepository.delete(idMariage);
	}

}
