package com.github.talbotgui.mariage.metier.dao;

import java.util.Collection;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.Mariage;

@Transactional
public interface MariageRepository extends CrudRepository<Mariage, Long> {

	@Query("select distinct c from Mariage m"//
			+ " join m.courriers c"//
			+ " left join fetch c.etapes"//
			+ " where m.id=:idMariage order by c.datePrevisionEnvoi")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Courrier> listerCourriersParIdMariageFetchEtapesDuCourrier(@Param("idMariage") Long idMariage);

	@Query("select e from Mariage m join m.etapes e where m.id=:idMariage order by e.numOrdre")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Etape> listerEtapesParIdMariage(@Param("idMariage") Long idMariage);

	@Query("select m from Utilisateur u join u.autorisations a join a.mariage m where u.login = :login")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Mariage> listerMariagesAutorises(@Param("login") String login);

	@Query("select m from Mariage m")
	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_READONLY, value = "true") })
	Collection<Mariage> listerTousMariages();

}
