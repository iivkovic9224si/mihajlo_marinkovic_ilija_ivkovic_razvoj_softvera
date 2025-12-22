package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.DrziPredmet;
import org.raflab.studsluzba.model.entity.Ispit;
import org.raflab.studsluzba.model.entity.IspitniRok;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IspitRepository extends CrudRepository<Ispit, Long> {

    Optional<Ispit> findByDrziPredmetAndIspitniRok(DrziPredmet drziPredmet, IspitniRok ispitniRok);

    // Nova metoda
    @Query("SELECT i FROM Ispit i WHERE i.ispitniRok.id = :rokId")
    List<Ispit> findByIspitniRokId(@Param("rokId") Long rokId);
}

