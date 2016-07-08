package com.github.talbotgui.mariage.rest.controleur.dto;

import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.entities.Courrier;

public class CourrierDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;

	private String datePrevisionEnvoi;
	private Long id;
	private String nom;

	public CourrierDTO() {
		super(null);
	}

	public CourrierDTO(final Object entity) {
		super(entity);

		final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME);
		if (entity != null) {
			final Courrier c = (Courrier) entity;
			this.id = c.getId();
			if (c.getDatePrevisionEnvoi() != null) {
				this.datePrevisionEnvoi = sdf.format(c.getDatePrevisionEnvoi());
			}
			this.nom = c.getNom();
		}
	}

	public String getDatePrevisionEnvoi() {
		return datePrevisionEnvoi;
	}

	public Long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public void setDatePrevisionEnvoi(final String datePrevisionEnvoi) {
		this.datePrevisionEnvoi = datePrevisionEnvoi;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}
}
