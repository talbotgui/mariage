package com.github.talbotgui.mariage.metier.dao;

public interface GeneriqueRepository {

	<T> T getReference(Class<T> clazz, Long id);

}
