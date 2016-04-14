package com.github.talbotgui.mariage.rest.controleur.dto;

import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.entities.Mariage;

public class MariageDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;

	private String dateCelebration;
	private Long id;
	private String marie1;
	private String marie2;

	public MariageDTO() {
		super(null);
	}

	public MariageDTO(final Object entity) {
		super(entity);

		if (entity != null) {
			final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
			final Mariage m = (Mariage) entity;
			this.id = m.getId();
			this.dateCelebration = sdf.format(m.getDateCelebration());
			this.marie1 = m.getMarie1();
			this.marie2 = m.getMarie2();
		}
	}

	public String getDateCelebration() {
		return dateCelebration;
	}

	public Long getId() {
		return id;
	}

	public String getMarie1() {
		return marie1;
	}

	public String getMarie2() {
		return marie2;
	}

	public String getNomMaries() {
		return marie1 + " & " + marie2;
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
