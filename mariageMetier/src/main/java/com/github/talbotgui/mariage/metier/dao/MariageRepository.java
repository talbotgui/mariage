package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Mariage;

public interface MariageRepository extends CrudRepository<Mariage, Long> {

	@Query("select e from Mariage m join m.etapes e where m.id=:idMariage order by e.dateHeure")
	Collection<Etape> listeEtapesParIdMariage(@Param("idMariage") Long idMariage);
}
