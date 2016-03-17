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
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.PresenceEtape;

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
	public Mariage chargeMariageParId(Long idMariage) {
		return this.mariageRepository.findOne(idMariage);
	}

	@Override
	public Collection<Etape> listeEtapesParIdMariage(Long idMariage) {
		return this.mariageRepository.listeEtapesParIdMariage(idMariage);
	}

	@Override
	public Collection<Invite> listeInvitesParIdMariage(Long idMariage) {
		return inviteRepository.listeInvitesParIdMariage(idMariage);
	}

	@Override
	public Page<Invite> listeInvitesParIdMariage(Long idMariage, Pageable page) {
		return inviteRepository.listeInvitesParIdMariage(idMariage, page);
	}

	@Override
	public Collection<Mariage> listeTousMariages() {
		Collection<Mariage> liste = new ArrayList<>();
		liste.addAll((Collection<Mariage>) this.mariageRepository.findAll());
		return liste;
	}

	@Override
	public Long sauvegarde(Mariage m) {
		return this.mariageRepository.save(m).getId();
	}

	@Override
	public Long sauvegardeGrappe(Mariage m) {
		Long id = this.mariageRepository.save(m).getId();
		for (Etape e : m.getEtapes()) {
			this.etapeRepository.save(e);
		}
		for (Invite i : m.getInvites()) {
			i.setMariage(m);
			this.inviteRepository.save(i);
			for (PresenceEtape pe : i.getPresencesEtape()) {
				this.presenceEtapeRepository.save(pe);
			}
		}
		return id;
	}

}
