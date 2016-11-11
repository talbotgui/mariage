package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Presence;

public class PresenceDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String commentaire;
	private Boolean confirmee;
	private String dateMaj;
	private Long idEtape;
	private Long idInvite;
	private String nomEtape;
	private String nomInvite;
	private String prenomInvite;
	private Boolean present;

	public PresenceDTO() {
		super();
	}

	public PresenceDTO(final Object p) {
		this((Presence) p);
	}

	public PresenceDTO(final Presence p) {
		final SimpleDateFormat sdf = new SimpleDateFormat(DTOUtils.FORMAT_DATE_TIME);
		this.idEtape = p.getId().getEtape().getId();
		this.idInvite = p.getId().getInvite().getId();
		if (p.getDateMaj() != null) {
			this.dateMaj = sdf.format(p.getDateMaj());
		}
		this.nomEtape = p.getId().getEtape().getNom();
		this.nomInvite = p.getId().getInvite().getNom();
		this.prenomInvite = p.getId().getInvite().getPrenom();
		this.commentaire = p.getCommentaire();
		this.confirmee = p.getConfirmee();
		this.present = p.getPresent();
	}

	public String getCommentaire() {
		return this.commentaire;
	}

	public Boolean getConfirmee() {
		return this.confirmee;
	}

	public String getDateMaj() {
		return this.dateMaj;
	}

	public String getId() {
		return this.idEtape + "-" + this.idInvite;
	}

	public Long getIdEtape() {
		return this.idEtape;
	}

	public Long getIdInvite() {
		return this.idInvite;
	}

	public String getNomEtape() {
		return this.nomEtape;
	}

	public String getNomInvite() {
		return this.nomInvite;
	}

	public String getPrenomInvite() {
		return this.prenomInvite;
	}

	public Boolean getPresent() {
		return this.present;
	}

	public void setCommentaire(final String commentaire) {
		this.commentaire = commentaire;
	}

	public void setConfirmee(final Boolean confirmee) {
		this.confirmee = confirmee;
	}

	public void setDateMaj(final String dateMaj) {
		this.dateMaj = dateMaj;
	}

	public void setIdEtape(final Long idEtape) {
		this.idEtape = idEtape;
	}

	public void setIdInvite(final Long idInvite) {
		this.idInvite = idInvite;
	}

	public void setNomEtape(final String nomEtape) {
		this.nomEtape = nomEtape;
	}

	public void setNomInvite(final String nomInvite) {
		this.nomInvite = nomInvite;
	}

	public void setPrenomInvite(final String prenomInvite) {
		this.prenomInvite = prenomInvite;
	}

	public void setPresent(final Boolean present) {
		this.present = present;
	}
}
