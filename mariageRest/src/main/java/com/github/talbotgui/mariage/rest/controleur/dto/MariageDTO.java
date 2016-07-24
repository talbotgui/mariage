package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;
import com.github.talbotgui.mariage.metier.entities.Mariage;

public class MariageDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String dateCelebration;
	private Long id;
	private String marie1;
	private String marie2;

	public MariageDTO() {
		super();
	}

	public MariageDTO(final Mariage m) {
		final SimpleDateFormat sdf = new SimpleDateFormat(DTOUtils.FORMAT_DATE);
		this.id = m.getId();
		this.dateCelebration = sdf.format(m.getDateCelebration());
		this.marie1 = m.getMarie1();
		this.marie2 = m.getMarie2();
	}

	public MariageDTO(final Object m) {
		this((Mariage) m);
	}

	public String getDateCelebration() {
		return this.dateCelebration;
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

	public String getNomMaries() {
		return this.marie1 + " & " + this.marie2;
	}

	public void setDateCelebration(final String dateCelebration) {
		this.dateCelebration = dateCelebration;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setMarie1(final String marie1) {
		this.marie1 = marie1;
	}

	public void setMarie2(final String marie2) {
		this.marie2 = marie2;
	}
}
