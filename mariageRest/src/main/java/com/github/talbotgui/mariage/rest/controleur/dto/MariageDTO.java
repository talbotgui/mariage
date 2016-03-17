package com.github.talbotgui.mariage.rest.controleur.dto;

import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.entities.Mariage;

public class MariageDTO extends AbstractDTO {

	private String dateCelebration;

	private Long id;

	private String marie1;

	private String marie2;

	public MariageDTO() {
		super(null);
	}

	public MariageDTO(Object entity) {
		super(entity);

		if (entity != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME);
			Mariage m = (Mariage) entity;
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

	public void setDateCelebration(String dateCelebration) {
		this.dateCelebration = dateCelebration;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMarie1(String marie1) {
		this.marie1 = marie1;
	}

	public void setMarie2(String marie2) {
		this.marie2 = marie2;
	}
}
