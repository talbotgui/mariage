package com.github.talbotgui.mariage.rest.controleur.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Hibernate;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;

public class CourrierDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;

	private String datePrevisionEnvoi;
	private Long id;
	private Collection<LienCourrierEtapeDTO> liensCourrierEtape = new ArrayList<>();
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

			if (c.getEtapesInvitation() != null && Hibernate.isInitialized(c.getEtapesInvitation())) {
				for (final Etape e : c.getEtapesInvitation()) {
					this.liensCourrierEtape.add(new LienCourrierEtapeDTO(c.getId(), e.getId(), true));
				}
			}
		}
	}

	public String getDatePrevisionEnvoi() {
		return datePrevisionEnvoi;
	}

	public Long getId() {
		return id;
	}

	public Collection<LienCourrierEtapeDTO> getLiensCourrierEtape() {
		return liensCourrierEtape;
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

	public void setLiensCourrierEtape(final Collection<LienCourrierEtapeDTO> liensCourrierEtape) {
		this.liensCourrierEtape = liensCourrierEtape;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}
}
