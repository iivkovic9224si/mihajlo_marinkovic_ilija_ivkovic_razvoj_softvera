package org.raflab.studsluzba.repositories;


import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudijskiProgramRepository extends CrudRepository<StudijskiProgram, Long> {
	
	@Query("select distinct oznaka from StudijskiProgram")
    List<String> findAllOznaka();
	
	@Query("select sp from StudijskiProgram sp order by sp.godinaAkreditacije desc")
	List<StudijskiProgram> getAllSortedByGodinaDesc();

	@Query("select sp from StudijskiProgram sp where sp.oznaka = :oznaka order by sp.godinaAkreditacije desc")
	List<StudijskiProgram> findByOznaka(@Param("oznaka") String oznaka);

    @Query("select sp from StudijskiProgram sp where sp.oznaka = :oznaka")
    StudijskiProgram findByOznakaSingle(@Param("oznaka") String oznaka);

	// spisak predmeta na studijskom programu
	@Query("select p " +
			"from Predmet p where p.studProgram = :studProgram")
	List<Predmet> findPredmetByStudProgram(StudijskiProgram studProgram);

}
