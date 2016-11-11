package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRESENCE")
public class Presence implements Serializable {
	private static final long serialVersionUID = 1L;

	private String commentaire;

	private Boolean confirmee;

	private Date dateMaj;

	@Id
	private PresenceId id;

	private Boolean present;

	protected Presence() {
		super();
	}

	public Presence(final Etape etape, final Invite invite) {
		super();
		this.setId(new PresenceId(etape, invite));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		return this.hashCode() == ((Presence) obj).hashCode();
	}

	public String getCommentaire() {
		return this.commentaire;
	}

	public Boolean getConfirmee() {
		return this.confirmee;
	}

	public Date getDateMaj() {
		return this.dateMaj;
	}

	public PresenceId getId() {
		return this.id;
	}

	public Boolean getPresent() {
		return this.present;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(this.id.getEtape().getId() + 1000 * this.id.getInvite().getId()).intValue();
	}

	public void setCommentaire(final String commentaire) {
		this.commentaire = commentaire;
	}

	public void setConfirmee(final Boolean confirmee) {
		this.confirmee = confirmee;
	}

	public void setDateMaj(final Date dateMaj) {
		this.dateMaj = dateMaj;
	}

	public void setId(final PresenceId id) {
		this.id = id;
	}

	public void setPresent(final Boolean present) {
		this.present = present;
	}

}
