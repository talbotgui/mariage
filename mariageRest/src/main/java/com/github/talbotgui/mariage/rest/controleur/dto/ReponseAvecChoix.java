package com.github.talbotgui.mariage.rest.controleur.dto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;

public class ReponseAvecChoix {

	private Collection<?> choixPossibles = new ArrayList<>();

	private Collection<DtoAvecChoix> dtos = new ArrayList<>();

	public ReponseAvecChoix() {
		super();
	}

	public ReponseAvecChoix(final Collection<?> choixPossibles, final Class<?> choixClass, final Collection<?> entites,
			final Class<?> dtoClass, final Method getterAttributDuChoix)
			throws IllegalArgumentException, ReflectiveOperationException {
		super();
		this.choixPossibles = DTOUtils.creerDtos(choixPossibles, choixClass);
		final Collection<DtoAvecChoix> dtosAvecChoix = new ArrayList<>();
		if (entites != null) {
			for (final Object entite : entites) {
				dtosAvecChoix.add(new DtoAvecChoix(entite, dtoClass, getterAttributDuChoix, choixPossibles));
			}
		}
		this.dtos = dtosAvecChoix;
	}

	public Collection<?> getChoixPossibles() {
		return this.choixPossibles;
	}

	public Collection<DtoAvecChoix> getDtos() {
		return this.dtos;
	}

	public void setChoixPossibles(final Collection<?> choixPossibles) {
		this.choixPossibles = choixPossibles;
	}

	public void setDtos(final Collection<DtoAvecChoix> dtos) {
		this.dtos = dtos;
	}

}
