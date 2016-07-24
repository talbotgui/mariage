package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Courrier;

public class CourrierDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String datePrevisionEnvoi;
	private Long id;
	private String nom;

	public CourrierDTO() {
		super();
	}

	public CourrierDTO(final Courrier c) {
		final SimpleDateFormat sdf = new SimpleDateFormat(DTOUtils.FORMAT_DATE_TIME);
		this.id = c.getId();
		if (c.getDatePrevisionEnvoi() != null) {
			this.datePrevisionEnvoi = sdf.format(c.getDatePrevisionEnvoi());
		}
		this.nom = c.getNom();

	}

	public CourrierDTO(final Object c) {
		this((Courrier) c);
	}

	public String getDatePrevisionEnvoi() {
		return this.datePrevisionEnvoi;
	}

	public Long getId() {
		return this.id;
	}

	public String getNom() {
		return this.nom;
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
