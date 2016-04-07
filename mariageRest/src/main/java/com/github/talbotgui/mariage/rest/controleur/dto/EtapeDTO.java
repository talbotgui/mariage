package com.github.talbotgui.mariage.rest.controleur.dto;

import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;

public class EtapeDTO extends AbstractDTO {

	private String celebrant;

	private String dateHeure;

	private Long id;

	private String lieu;

	private String nom;

	private String type;

	public EtapeDTO() {
		super(null);
	}

	public EtapeDTO(Object entity) {
		super(entity);

		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		if (entity != null) {
			Etape e = (Etape) entity;
			this.id = e.getId();
			this.dateHeure = sdf.format(e.getDateHeure());
			this.nom = e.getNom();
			this.lieu = e.getLieu();
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

	public String getType() {
		return type;
	}

	public void setCelebrant(String celebrant) {
		this.celebrant = celebrant;
	}

	public void setDateHeure(String dateHeure) {
		this.dateHeure = dateHeure;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setType(String type) {
		this.type = type;
	}

}
