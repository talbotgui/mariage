package com.github.talbotgui.mariage.rest.controleur.dto;

import com.github.talbotgui.mariage.metier.entities.PresenceEtape;

public class PresenceEtapeDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;

	private Long idEtape;

	private Long idPresenceEtape;

	private Boolean presence;

	public PresenceEtapeDTO() {
		super(null);
	}

	public PresenceEtapeDTO(final Object entity) {
		super(entity);

		if (entity != null) {
			final PresenceEtape pe = (PresenceEtape) entity;
			this.idEtape = pe.getEtape().getId();
			this.idPresenceEtape = pe.getId();
			this.presence = pe.getPresent();
		}
	}

	public Long getIdEtape() {
		return idEtape;
	}

	public Long getIdPresenceEtape() {
		return idPresenceEtape;
	}

	public Boolean getPresence() {
		return presence;
	}

	public void setIdEtape(final Long idEtape) {
		this.idEtape = idEtape;
	}

	public void setIdPresenceEtape(final Long idPresenceEtape) {
		this.idPresenceEtape = idPresenceEtape;
	}

	public void setPresence(final Boolean presence) {
		this.presence = presence;
	}

}
