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

@Entity
public class Mariage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dateCelebration;

	@OneToMany(mappedBy = "mariage")
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

	public Mariage(Date dateCelebration, String marie1, String marie2) {
		super();
		this.dateCelebration = dateCelebration;
		this.marie1 = marie1;
		this.marie2 = marie2;
	}

	public Mariage(Long id, Date dateCelebration, String marie1, String marie2) {
		this(dateCelebration, marie1, marie2);
		this.id = id;
	}

	public void addEtape(Etape e) {
		this.etapes.add(e);
	}

	public void addInvite(Invite i) {
		this.invites.add(i);
	}

	public Date getDateCelebration() {
		return dateCelebration;
	}

	public Collection<Etape> getEtapes() {
		return etapes;
	}

	public Long getId() {
		return id;
	}

	public Collection<Invite> getInvites() {
		Collection<Invite> c = new ArrayList<>();
		c.addAll(this.invites);
		return c;
	}

	public String getMarie1() {
		return marie1;
	}

	public String getMarie2() {
		return marie2;
	}

	public void setDateCelebration(Date dateCelebration) {
		this.dateCelebration = dateCelebration;
	}

	public void setEtapes(Collection<Etape> etapes) {
		this.etapes = etapes;
	}

	public void setInvites(Collection<Invite> invites) {
		this.invites = invites;
	}

	public void setMarie1(String marie1) {
		this.marie1 = marie1;
	}

	public void setMarie2(String marie2) {
		this.marie2 = marie2;
	}

}
