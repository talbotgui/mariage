package com.github.talbotgui.mariage.metier.dao.securite;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;

public interface AutorisationRepository extends CrudRepository<Autorisation, Long> {

	@Query("select a from Autorisation a left join fetch a.mariage left join fetch a.utilisateur u order by u.login")
	Collection<Autorisation> listerAutorisations();

}
