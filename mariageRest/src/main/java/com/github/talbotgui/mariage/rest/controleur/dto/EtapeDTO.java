package com.github.talbotgui.mariage.rest.controleur.dto;

import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;

public class EtapeDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;

	private String celebrant;
	private String dateHeure;
	private Long id;
	private String lieu;
	private String nom;
	private String numOrdre;
	private String type;

	public EtapeDTO() {
		super(null);
	}

	public EtapeDTO(final Object entity) {
		super(entity);

		final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		if (entity != null) {
			final Etape e = (Etape) entity;
			this.id = e.getId();
			this.dateHeure = sdf.format(e.getDateHeure());
			this.nom = e.getNom();
			this.lieu = e.getLieu();
			if (e.getNumOrdre() != null) {
				this.numOrdre = e.getNumOrdre().toString();
			}
			this.type = e.getClass().getSimpleName();

			if (e instanceof EtapeCeremonie) {
				this.celebrant = ((EtapeCeremonie) e).getCelebrant();
			}
		}
	}

	public String getCelebrant() {
		return celebrant;
	}

	public String getDateHeure() {
		return dateHeure;
	}

	public Long getId() {
		return id;
	}

	public String getLieu() {
		return lieu;
	}

	public String getNom() {
		return nom;
	}

	public String getNumOrdre() {
		return numOrdre;
	}

	public String getType() {
		return type;
	}

	public void setCelebrant(final String celebrant) {
		this.celebrant = celebrant;
	}

	public void setDateHeure(final String dateHeure) {
		this.dateHeure = dateHeure;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setLieu(final String lieu) {
		this.lieu = lieu;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setNumOrdre(final String numOrdre) {
		this.numOrdre = numOrdre;
	}

	public void setType(final String type) {
		this.type = type;
	}

}
