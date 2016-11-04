package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;

@Entity
public class Mariage implements Serializable {
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "mariage")
	private Collection<Autorisation> autorisations = new ArrayList<>();

	@OneToMany(mappedBy = "mariage")
	@OrderBy("datePrevisionEnvoi")
	private Collection<Courrier> courriers = new ArrayList<>();

	@Column(name = "DATE_CELEBRATION")
	private Date dateCelebration;

	@OneToMany(mappedBy = "mariage")
	@OrderBy("numOrdre")
	private Collection<Etape> etapes = new ArrayList<>();

	@OneToMany(mappedBy = "mariage")
	private Collection<Foyer> foyers = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String marie1;

	private String marie2;

	protected Mariage() {
		super();
	}

	public Mariage(final Date dateCelebration, final String marie1, final String marie2) {
		super();
		if (dateCelebration != null) {
			this.setDateCelebration(dateCelebration);
		}
		this.setMarie1(marie1);
		this.setMarie2(marie2);
	}

	public Mariage(final Long id, final Date dateCelebration, final String marie1, final String marie2) {
		this(dateCelebration, marie1, marie2);
		this.setId(id);
	}

	public void addCourrier(final Courrier c) {
		this.courriers.add(c);
	}

	public void addEtape(final Etape e) {
		this.etapes.add(e);
	}

	public void addFoyer(final Foyer f) {
		this.foyers.add(f);
	}

	public Collection<Autorisation> getAutorisations() {
		final Collection<Autorisation> a = new ArrayList<>();
		a.addAll(this.autorisations);
		return a;
	}

	public Collection<Courrier> getCourriers() {
		final Collection<Courrier> c = new ArrayList<>();
		c.addAll(this.courriers);
		return c;
	}

	public Date getDateCelebration() {
		if (this.dateCelebration != null) {
			return new Date(this.dateCelebration.getTime());
		}
		return null;
	}

	public Collection<Etape> getEtapes() {
		final Collection<Etape> e = new ArrayList<>();
		e.addAll(this.etapes);
		return e;
	}

	public Collection<Foyer> getFoyers() {
		final Collection<Foyer> c = new ArrayList<>();
		c.addAll(this.foyers);
		return c;
	}

	public Long getId() {
		return this.id;
	}

	public String getMarie1() {
		return this.marie1;
	}

	public String getMarie2() {
		return this.marie2;
	}

	public void setAutorisations(final Collection<Autorisation> autorisations) {
		this.autorisations = new ArrayList<>(autorisations);
	}

	public void setCourriers(final Collection<Courrier> courriers) {
		this.courriers = new ArrayList<>(courriers);
	}

	public void setDateCelebration(final Date dateCelebration) {
		if (dateCelebration != null) {
			this.dateCelebration = new Date(dateCelebration.getTime());
		}
	}

	public void setEtapes(final Collection<Etape> etapes) {
		this.etapes = new ArrayList<>(etapes);
	}

	public void setFoyers(final Collection<Foyer> foyers) {
		this.foyers = new ArrayList<>(foyers);
	}

	private void setId(final Long id) {
		this.id = id;
	}

	public void setMarie1(final String marie1) {
		this.marie1 = marie1;
	}

	public void setMarie2(final String marie2) {
		this.marie2 = marie2;
	}

}
