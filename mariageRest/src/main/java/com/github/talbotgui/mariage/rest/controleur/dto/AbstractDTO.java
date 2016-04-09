package com.github.talbotgui.mariage.rest.controleur.dto;

import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractDTO {

	public static final String FORMAT_DATE = "dd/MM/yyyy";

	public static final String FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm";

	public static <T> Collection<T> creerDto(final Collection<?> entities, final Class<T> clazz) {
		final Collection<T> col = new ArrayList<>();
		if (entities != null) {
			for (final Object entity : entities) {
				try {
					col.add(clazz.getConstructor(Object.class).newInstance(entity));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new InvalidParameterException("Erreur de code");
				}
			}
		}
		return col;
	}

	public AbstractDTO(final Object entity) {
	}
}
