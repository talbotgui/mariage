package com.github.talbotgui.mariage.rest.controleur.dto;

public class LienCourrierEtapeDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;

	private Long idCourrier;

	private Long idEtape;
	private Boolean lien;

	public LienCourrierEtapeDTO(final Long idCourrier, final Long idEtape, final Boolean lien) {
		super(null);
		this.idCourrier = idCourrier;
		this.idEtape = idEtape;
		this.lien = lien;
	}

	public Long getIdCourrier() {
		return idCourrier;
	}

	public Long getIdEtape() {
		return idEtape;
	}

	public Boolean getLien() {
		return lien;
	}

	public void setIdCourrier(final Long idCourrier) {
		this.idCourrier = idCourrier;
	}

	public void setIdEtape(final Long idEtape) {
		this.idEtape = idEtape;
	}

	public void setLien(final Boolean lien) {
		this.lien = lien;
	}
}
