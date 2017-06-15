package com.github.talbotgui.mariage.metier.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class GeneriqueRepositoryImpl implements GeneriqueRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public <T> T getReference(final Class<T> clazz, final Long id) {
		return this.em.getReference(clazz, id);
	}

}
