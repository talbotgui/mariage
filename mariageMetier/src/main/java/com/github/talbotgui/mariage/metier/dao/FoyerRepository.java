package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.github.talbotgui.mariage.metier.entities.Foyer;

@Transactional
public interface FoyerRepository extends PagingAndSortingRepository<Foyer, Long> {

	@Query("delete Foyer where id in (select f.id from Foyer f where f.mariage.id = :idMariage)")
	@Modifying
	void deleteFoyersParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select f" //
			+ " from Mariage m join m.foyers f"//
			+ " where m.id = :idMariage"//
			+ " and f.nom = :nomFoyer")
	Foyer findOneParNom(@Param("idMariage") Long idMariage, @Param("nomFoyer") String nomFoyer);

	@Query("select f" //
			+ " from Mariage m join m.courriers c"//
			+ " join c.foyersInvites f"//
			+ " where m.id=:idMariage"//
			+ " and c.id=:idCourrier"//
			+ " order by f.groupe, f.nom")
	Collection<Foyer> listeFoyersParIdCourrier(@Param("idMariage") Long idMariage,
			@Param("idCourrier") Long idCourrier);

	@Query("select f" //
			+ " from Mariage m join m.foyers f"//
			+ " where m.id=:idMariage"//
			+ " order by f.groupe, f.nom")
	Collection<Foyer> listeFoyersParIdMariage(@Param("idMariage") Long idMariage);

}
