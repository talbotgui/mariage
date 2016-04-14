package com.github.talbotgui.mariage.metier.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
	private Collection<Autorisation> autorisations;

	private Date dateCelebration;

	@OneToMany(mappedBy = "mariage")
	@OrderBy("numOrdre")
	private Collection<Etape> etapes = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy = "mariage")
	private Collection<Invite> invites = new ArrayList<>();

	private String marie1;

	private String marie2;

	protected Mariage() {
		super();
	}

	public Mariage(final Date dateCelebration, final String marie1, final String marie2) {
		super();
		if (dateCelebration != null) {
			this.dateCelebration = new Date(dateCelebration.getTime());
		}
		this.marie1 = marie1;
		this.marie2 = marie2;
	}

	public Mariage(final Long id, final Date dateCelebration, final String marie1, final String marie2) {
		this(dateCelebration, marie1, marie2);
		this.id = id;
	}

	public void addEtape(final Etape e) {
		this.etapes.add(e);
	}

	public void addInvite(final Invite i) {
		this.invites.add(i);
	}

	public Collection<Autorisation> getAutorisations() {
		return autorisations;
	}

	public Date getDateCelebration() {
		if (this.dateCelebration != null) {
			return new Date(this.dateCelebration.getTime());
		}
		return null;
	}

	public Collection<Etape> getEtapes() {
		return etapes;
	}

	public Long getId() {
		return id;
	}

	public Collection<Invite> getInvites() {
		final Collection<Invite> c = new ArrayList<>();
		c.addAll(this.invites);
		return c;
	}

	public String getMarie1() {
		return marie1;
	}

	public String getMarie2() {
		return marie2;
	}

	public void setAutorisations(final Collection<Autorisation> autorisations) {
		this.autorisations = autorisations;
	}

	public void setDateCelebration(final Date dateCelebration) {
		if (dateCelebration != null) {
			this.dateCelebration = new Date(dateCelebration.getTime());
		}
	}

	public void setEtapes(final Collection<Etape> etapes) {
		this.etapes = etapes;
	}

	public void setInvites(final Collection<Invite> invites) {
		this.invites = invites;
	}

	public void setMarie1(final String marie1) {
		this.marie1 = marie1;
	}

	public void setMarie2(final String marie2) {
		this.marie2 = marie2;
	}

}
