package com.github.talbotgui.mariage.metier.dto;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.github.talbotgui.mariage.metier.exception.BusinessException;

public abstract class DTOUtils implements Serializable {
	public static final String FORMAT_DATE = "dd/MM/yyyy";

	public static final String FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm";

	private static final long serialVersionUID = 1L;

	/**
	 * Copie tous les attributs passés en paramètres de la source vers la cible.
	 *
	 * @param source
	 *            Objet source des données
	 * @param cible
	 *            Objet qui sera modfié
	 * @param includesArray
	 *            Liste des attributs à inclure
	 * @param bloqueEcrasementNull
	 *            empeche la mise à null d'un attribut renseigné
	 */
	public static void copyBeanProperties(final Object source, final Object cible, final boolean bloqueEcrasementNull,
			final String... includesArray) {
		final Collection<String> includes = Arrays.asList(includesArray);

		final PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
		final Collection<String> excludes = new ArrayList<String>();
		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (!includes.contains(propertyDescriptor.getName())) {
				excludes.add(propertyDescriptor.getName());
			} else if (bloqueEcrasementNull) {
				Object valSource;
				Object valCible;
				try {
					valSource = propertyDescriptor.getReadMethod().invoke(source);
					valCible = propertyDescriptor.getReadMethod().invoke(cible);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new BusinessException(BusinessException.ERREUR_TRANSFORMATION_MODEL, e);
				}
				if (valSource == null && valCible != null) {
					excludes.add(propertyDescriptor.getName());
				}
			}
		}

		BeanUtils.copyProperties(source, cible, excludes.toArray(new String[excludes.size()]));
	}

	public static <T> T creerDto(final Object entity, final Class<T> clazz) {
		try {
			return clazz.getConstructor(Object.class).newInstance(entity);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new BusinessException(BusinessException.ERREUR_TRANSFORMATION_MODEL, e);
		}
	}

	public static <T> List<T> creerDtos(final Collection<?> entities, final Class<T> clazz) {
		final List<T> col = new ArrayList<>();
		if (entities != null) {
			for (final Object entity : entities) {
				col.add(creerDto(entity, clazz));
			}
		}
		return col;
	}

	public static <T> Page<T> creerDtos(final Pageable request, final Page<?> response, final Class<T> clazz) {
		final List<T> elements = creerDtos(response.getContent(), clazz);
		return new PageImpl<T>(elements, request, response.getTotalElements());
	}

	private DTOUtils() {
	}
}
