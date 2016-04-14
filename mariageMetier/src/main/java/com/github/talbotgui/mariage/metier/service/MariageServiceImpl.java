package com.github.talbotgui.mariage.metier.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.talbotgui.mariage.metier.dao.EtapeRepository;
import com.github.talbotgui.mariage.metier.dao.InviteRepository;
import com.github.talbotgui.mariage.metier.dao.MariageRepository;
import com.github.talbotgui.mariage.metier.dao.PresenceEtapeRepository;
import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.PresenceEtape;
import com.github.talbotgui.mariage.metier.exception.BusinessException;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class MariageServiceImpl implements MariageService {

	@Autowired
	private EtapeRepository etapeRepository;

	@Autowired
	private InviteRepository inviteRepository;

	@Autowired
	private MariageRepository mariageRepository;

	@Autowired
	private PresenceEtapeRepository presenceEtapeRepository;

	@Override
	public Mariage chargeMariageParId(final Long idMariage) {
		return this.mariageRepository.findOne(idMariage);
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
		this.inviteRepository.updateAdresseEtTelephoneParFoyer(invite.getFoyer(), invite.getAdresse(),
				invite.getTelephone());

		return id;
	}

	@Override
	public Long sauvegarde(final Mariage m) {
		return this.mariageRepository.save(m).getId();
	}

	@Override
	public Long sauvegardeGrappe(final Mariage m) {
		final Long id = this.sauvegarde(m);

		for (final Etape e : m.getEtapes()) {
			this.sauvegarde(id, e);
		}

		for (final Invite i : m.getInvites()) {
			this.sauvegarde(id, i);
		}

		return id;
	}

	@Override
	public void suprimeEtape(final Long idMariage, final Long idEtape) {
		if (idMariage == null || !idMariage.equals(this.etapeRepository.getIdMariageByEtapeId(idEtape))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}
		this.etapeRepository.delete(idEtape);
	}

	@Override
	public void suprimeInvite(final Long idMariage, final Long idInvite) {
		if (idMariage == null || !idMariage.equals(this.inviteRepository.getIdMariageByInviteId(idInvite))) {
			throw new BusinessException(BusinessException.ERREUR_ID_MARIAGE, new Object[] { idMariage });
		}
		this.inviteRepository.delete(idInvite);
	}

}
