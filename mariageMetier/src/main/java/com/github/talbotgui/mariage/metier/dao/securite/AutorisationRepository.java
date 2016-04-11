package com.github.talbotgui.mariage.metier.dao.securite;

import org.springframework.data.repository.CrudRepository;

import com.github.talbotgui.mariage.metier.entities.securite.Autorisation;

public interface AutorisationRepository extends CrudRepository<Autorisation, Long> {

}
