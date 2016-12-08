package com.github.talbotgui.mariage.metier.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import com.github.talbotgui.mariage.metier.dto.StatistiquesPresenceMariage;
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
	public StatistiquesMariage calculerStatistiques(final Long idMariage) {
		final StatistiquesInvitesMariage invites = this.inviteRepository.calculerStatistiquesInvites(idMariage);

		final Map<String, Integer> nbInvitesParGroupe = this
				.arrayToMap(this.inviteRepository.compterNombreInviteParGroupe(idMariage));
		final Map<String, Integer> nbInvitesParFoyer = this
				.arrayToMap(this.inviteRepository.compterNombreInviteParFoyer(idMariage));
		final Map<String, Integer> nbInvitesParAge = this
				.arrayToMap(this.inviteRepository.compterNombreInviteParAge(idMariage));
		final Map<String, Integer> nbInvitesParEtape = this
				.arrayToMap(this.inviteRepository.compterNombreInviteParEtape(idMariage));
		final Map<String, Integer> nbFoyersParEtape = this
				.arrayToMap(this.inviteRepository.compterNombreFoyerParEtape(idMariage));
		final StatistiquesRepartitionsInvitesMariage repartition = new StatistiquesRepartitionsInvitesMariage(
				nbInvitesParAge, nbInvitesParFoyer, nbInvitesParGroupe, nbInvitesParEtape, nbFoyersParEtape);
		return new StatistiquesMariage(repartition, invites);
	}

	@Override
	public Collection<StatistiquesPresenceMariage> calculerStatistiquesPresence(final Long idMariage,
			final Long idEtape) {
		return this.presenceRepository.calculerStatistiquesPresence(idMariage, idEtape);
	}

	@Override
	public Foyer chargerFoyerParId(final Long id) {
		return this.foyerRepository.findOne(id);
	}

	@Override
	public Invite chargerInviteParId(final Long id) {
		return this.inviteRepository.findOne(id);
	}

	@Override
	public Mariage chargerMariageParId(final Long idMariage) {
		return this.mariageRepository.findOne(idMariage);
	}

	@Override
	public Presence chargerPresenceParEtapeEtInvite(final Long idMariage, final Long idEtape, final Long idInvite) {
		return this.presenceRepository.chargerPresenceParEtapeEtInvite(idMariage, idEtape, idInvite);
	}

	@Override
	public String genererPublipostage(final Long idMariage, final Long idCourrier) {
		final StringBuilder contenu = new StringBuilder("NOM;RUE;VILLE");

		final Collection<Foyer> foyersInvite = this.courrierRepository
				.rechercherFoyersInvitesByIdCourrierFetchInvites(idMariage, idCourrier);

		for (final Foyer f : foyersInvite) {
			contenu.append("\n");
			contenu.append(f.genereLignePublipostage());
		}

		return contenu.toString();
	}

	@Override
	public Foyer getFoyer(final Long idMariage, final String nomFoyer) {
		return this.foyerRepository.rechercherFoyer(idMariage, nomFoyer);
	}

	@Override
	public void lierUneEtapeEtUnCourrier(final Long idMariage, final Long idEtape, final Long idCourrier,
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
	public void lierUnFoyerEtUnCourrier(final Long idMariage, final Long idCourrier, final Long idFoyer,
			final boolean invitation) {
		final Invitation fei = this.invitationRepository.rechercherInvitation(idMariage, idCourrier, idFoyer);
		if (fei != null && !invitation) {
			this.invitationRepository.delete(fei);
		} else if (fei == null && invitation) {
			this.invitationRepository.save(new Invitation(new Courrier(idCourrier), new Foyer(idFoyer)));
		}
	}

	@Override
	public Collection<String> listerAgePossible() {
		final Collection<String> liste = new ArrayList<>();
		for (final Age a : Age.values()) {
			liste.add(a.name());
		}
		return liste;
	}

	@Override
	public Collection<Courrier> listerCourriersParIdMariage(final Long idMariage) {
		return this.mariageRepository.listerCourriersParIdMariageFetchEtapesDuCourrier(idMariage);
	}

	@Override
	public Collection<Etape> listerEtapesParIdMariage(final Long idMariage) {
		return this.mariageRepository.listerEtapesParIdMariage(idMariage);
	}

	@Override
	public Collection<Foyer> listerFoyersParIdCourrier(final Long idMariage, final Long idCourrier) {
		return this.foyerRepository.listerFoyersParIdCourrier(idMariage, idCourrier);
	}

	@Override
	public Collection<Foyer> listerFoyersParIdMariage(final Long idMariage) {
		return this.foyerRepository.listerFoyersParIdMariage(idMariage);
	}

	@Override
	public Collection<Invite> listerInvitesParIdMariage(final Long idMariage) {
		return this.inviteRepository.listerInvitesParIdMariage(idMariage);
	}

	@Override
	public Page<Invite> listerInvitesParIdMariage(final Long idMariage, final Pageable page) {
		return this.inviteRepository.listerInvitesParIdMariage(idMariage, page);
	}

	@Override
	public Collection<Presence> listerPresencesParIdMariage(final Long idMariage) {

		// Collection supprimant les doublons
		final Set<Presence> presenceFiltrees = new HashSet<>();

		// Ajout des presences existantes
		presenceFiltrees.addAll(this.presenceRepository.listerPresencesParIdMariage(idMariage));

		// Ajout de toutes les autres presences possibles avec le produit
		// cartesien (celles déjà présentes restent et pas de doublon avec
		// Presence.hashcode)
		presenceFiltrees.addAll(this.presenceRepository.listerProduitCartesienInviteEtEtapeParIdMariage(idMariage));

		// Tri
		final Comparator<Presence> comparator = (final Presence p1,
				final Presence p2) -> (p1.getId().getInvite().getNom() + p1.getId().getInvite().getPrenom()
						+ p1.getId().getEtape().getNumOrdre())
								.compareTo(p2.getId().getInvite().getNom() + p2.getId().getInvite().getPrenom()
										+ p2.getId().getEtape().getNumOrdre());
		final List<Presence> l = new ArrayList<>(presenceFiltrees);
		Collections.sort(l, comparator);

		return l;
	}

	@Override
	public Collection<Mariage> listerTousMariages() {
		final Collection<Mariage> liste = new ArrayList<>();
		liste.addAll((Collection<Mariage>) this.mariageRepository.findAll());
		return liste;
	}

	@Override
	public Collection<String> rechercherErreurs(final Long idMariage) {
		final Collection<String> erreurs = new ArrayList<>();

		// Recherche de personnes invités à plusieurs étapes.
		erreurs.addAll(this.rechercheErreursPourInviteSurPlusieursEtapes(idMariage));

		// Recherche les presences existantes sans invitations existantes.
		erreurs.addAll(this.recherchePresencesSansInvitations(idMariage));

		return erreurs;
	}

	/**
	 * Recherche de personnes invités à plusieurs étapes.
	 *
	 * @param idMariage
	 *            Identifiant du mariage
	 * @return une liste d'erreur en francais (pas d'i18n)
	 */
	private Collection<String> rechercheErreursPourInviteSurPlusieursEtapes(final Long idMariage) {
		final Collection<String> erreurs = new ArrayList<>();

		// invites sur plusieurs étapes
		String messageErreur = "";
		Long idInvitePrecedent = null;
		for (final Object[] objets : this.inviteRepository.rechercherInviteSurPlusieursEtapes(idMariage)) {
			final Long idInvite = (Long) objets[0];
			final String nomInvite = (String) objets[1];
			final String prenomInvite = (String) objets[2];
			final String nomEtape = (String) objets[3];

			// Si on change d'invite, on sauvegarde le precedent et on reinit
			if (!idInvite.equals(idInvitePrecedent)) {
				if (messageErreur.length() > 0) {
					erreurs.add(messageErreur);
				}
				messageErreur = String.format("%s %s est invité(e) plusieurs fois à une même étape : %s", //
						prenomInvite, nomInvite, nomEtape);
			}
			// Pour le même invite,on ajoute le nom de l'étape
			else {
				messageErreur += ", " + nomEtape;
			}

			idInvitePrecedent = idInvite;
		}

		// Sauvegarde du message d'erreur (si présent)
		if (messageErreur.length() > 0) {
			erreurs.add(messageErreur);
		}

		return erreurs;
	}

	@Override
	public Collection<Invite> listerInvitesPresentsParIdMariage(final Long idMariage) {
		return this.inviteRepository.listerInvitesPresentsParIdMariage(idMariage);
	}

	/**
	 * Recherche de présences sans invitations existantes.
	 *
	 * @param idMariage
	 *            Identifiant du mariage
	 * @return une liste d'erreur en francais (pas d'i18n)
	 */
	private Collection<String> recherchePresencesSansInvitations(final Long idMariage) {
		final Collection<String> erreurs = new ArrayList<>();

		// invites sur plusieurs étapes
		String messageErreur = "";
		Long idInvitePrecedent = null;
		for (final Object[] objets : this.presenceRepository.rechercherPresencesSansInvitations(idMariage)) {
			final Long idInvite = (Long) objets[0];
			final String nomInvite = (String) objets[1];
			final String prenomInvite = (String) objets[2];
			final String nomEtape = (String) objets[3];

			// Si on change d'invite, on sauvegarde le precedent et on reinit
			if (!idInvite.equals(idInvitePrecedent)) {
				if (messageErreur.length() > 0) {
					erreurs.add(messageErreur);
				}
				messageErreur = String.format(
						"%s %s est marqué(e) présent/absent, sans plus y être invité(e), à l'étape '%s'", //
						prenomInvite, nomInvite, nomEtape);
			}
			// Pour le même invite,on ajoute le nom de l'étape
			else {
				messageErreur += ", '" + nomEtape + "'";
			}

			idInvitePrecedent = idInvite;
		}

		// Sauvegarde du message d'erreur (si présent)
		if (messageErreur.length() > 0) {
			erreurs.add(messageErreur);
		}

		return erreurs;
	}

	@Override
	public Long sauvegarder(final Long idMariage, final Courrier courrier) {
		courrier.setMariage(this.mariageRepository.findOne(idMariage));
		return this.courrierRepository.save(courrier).getId();
	}

	@Override
	public Long sauvegarder(final Long idMariage, final Etape etape) {
		etape.setMariage(this.mariageRepository.findOne(idMariage));
		if (etape.getNumOrdre() == null) {
			etape.setNumOrdre(1 + this.etapeRepository.compterEtapeByIdMariage(idMariage));
		}
		return this.etapeRepository.save(etape).getId();
	}

	@Override
	public Long sauvegarder(final Long idMariage, final Foyer foyer) {
		foyer.setMariage(this.mariageRepository.findOne(idMariage));
		return this.foyerRepository.save(foyer).getId();

	}

	@Override
	public void sauvegarder(final Long idMariage, final Presence presence) {
		presence.setDateMaj(new Date());
		this.presenceRepository.save(presence);
	}

	@Override
	public Long sauvegarder(final Mariage m) {
		return this.mariageRepository.save(m).getId();
	}

	@Override
	public void sauvegardeEnMasse(final Long idMariage, final Collection<Invite> invites) {
		if (invites != null) {
			for (final Invite invite : invites) {
				this.sauvegarderInviteEtFoyer(idMariage, invite);
			}
		}
	}

	@Override
	public Long sauvegarderGrappe(final Mariage m) {
		final Long id = this.sauvegarder(m);

		for (final Etape e : m.getEtapes()) {
			this.sauvegarder(id, e);
		}

		for (final Courrier c : m.getCourriers()) {
			this.sauvegarder(id, c);
		}

		for (final Foyer f : m.getFoyers()) {
			for (final Invite i : f.getInvites()) {
				this.sauvegarderInviteEtFoyer(id, i);
			}
			for (final Courrier c : f.getCourriersInvitation()) {
				this.lierUnFoyerEtUnCourrier(id, c.getId(), f.getId(), true);
			}
		}

		return id;
	}

	@Override
	public Long sauvegarderInviteEtFoyer(final Long idMariage, final Invite invite) {

		if (invite.getFoyer().getId() != null) {
			final Foyer f = this.foyerRepository.findOne(invite.getFoyer().getId());
			DTOUtils.copyBeanProperties(invite.getFoyer(), f, true, "adresse", "email", "groupe", "nom", "telephone");
			invite.setFoyer(f);
		}
		this.sauvegarder(idMariage, invite.getFoyer());

		return this.inviteRepository.save(invite).getId();
	}

	@Override
	public void supprimerCourrier(final Long idMariage, final Long idCourrier) {
		if (idMariage == null || !idMariage.equals(this.courrierRepository.rechercherIdMariageByCourrierId(idCourrier))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}

		this.invitationRepository.supprimerInvitationsParIdCourrier(idCourrier);

		this.courrierRepository.delete(idCourrier);
	}

	@Override
	public void supprimerEtape(final Long idMariage, final Long idEtape) {
		if (idMariage == null || !idMariage.equals(this.etapeRepository.rechercherIdMariageByEtapeId(idEtape))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}
		if (this.etapeRepository.compterCourriersInvitant(idEtape) > 0) {
			throw new BusinessException(BusinessException.ERREUR_COURRIER_LIE_A_ETAPE, new Object[] {});
		}

		// Suppression des presences lies
		this.presenceRepository.supprimerPresencesParIdEtape(idEtape);

		this.etapeRepository.delete(idEtape);
	}

	private void supprimefoyer(final Long idFoyer) {

		// Supprime les invitations
		this.invitationRepository.supprimerInvitationsParIdFoyer(idFoyer);

		// Supprime le foyer
		final Foyer f = this.foyerRepository.findOne(idFoyer);
		this.foyerRepository.delete(f);
	}

	@Override
	public void supprimerInvite(final Long idMariage, final Long idInvite) {

		// Verification de la coherence des ID
		if (idMariage == null || !idMariage.equals(this.inviteRepository.rechercherIdMariageByInviteId(idInvite))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}

		// Suppression du foyer ?
		final Foyer f = this.inviteRepository.findOne(idInvite).getFoyer();
		final boolean supprimeFoyer = f.getInvites().size() == 1;

		// Supprime l'invite
		this.inviteRepository.delete(idInvite);

		// Supprime eventuellement le foyer
		if (supprimeFoyer) {
			this.supprimefoyer(f.getId());
		}
	}

	@Override
	public void supprimerMariage(final Long idMariage) {
		this.presenceRepository.supprimerPresencesParIdMariage(idMariage);
		this.courrierRepository.supprimerCourriersParIdMariage(idMariage);
		this.invitationRepository.supprimerInvitationParIdMariage(idMariage);
		this.etapeRepository.supprimerEtapesParIdMariage(idMariage);
		this.inviteRepository.supprimerInvitesParIdMariage(idMariage);
		this.foyerRepository.supprimerFoyersParIdMariage(idMariage);
		this.mariageRepository.delete(idMariage);
	}

	@Override
	public void supprimerPresence(final Long idMariage, final Long idInvite, final Long idEtape) {
		final Presence presence = this.presenceRepository.chargerPresenceParEtapeEtInvite(idMariage, idEtape, idInvite);
		if (presence != null) {
			this.presenceRepository.delete(presence);
		}
	}

}
