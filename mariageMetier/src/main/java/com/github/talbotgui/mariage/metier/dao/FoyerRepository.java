package com.github.talbotgui.mariage.metier.dao;

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

}
