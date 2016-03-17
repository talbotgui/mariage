package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PresenceEtape implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	private Etape etape;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Boolean present;

	protected PresenceEtape() {
		super();
	}

	public PresenceEtape(Etape etape, Boolean present) {
		super();
		this.etape = etape;
		this.present = present;
	}

	public Etape getEtape() {
		return etape;
	}

	public Long getId() {
		return id;
	}

	public Boolean getPresent() {
		return present;
	}

	public void setEtape(Etape etape) {
		this.etape = etape;
	}

	public void setPresent(Boolean present) {
		this.present = present;
	}

}
