package com.github.talbotgui.mariage.metier.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Etape {

	private Date dateHeure;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String lieu;

	private String nom;

	protected Etape() {
		super();
	}

	public Etape(String nom, Date dateHeure, String lieu) {
		super();
		this.dateHeure = dateHeure;
		this.nom = nom;
		this.lieu = lieu;
	}

	public Date getDateHeure() {
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

	public void setDateHeure(Date dateHeure) {
		this.dateHeure = dateHeure;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
