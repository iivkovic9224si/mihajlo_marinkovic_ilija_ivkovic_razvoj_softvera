package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.IspitniRok;
import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IspitniRokRepository extends CrudRepository<IspitniRok, Long> {
    List<IspitniRok> findBySkolskaGodina(SkolskaGodina aktivna);

    @Query("SELECT ir FROM IspitniRok ir " +
            "WHERE ir.skolskaGodina = :skolskaGodina " +
            "AND :datum BETWEEN ir.pocetak AND ir.kraj")
    List<IspitniRok> findBySkolskaGodinaAndDatum(@Param("skolskaGodina") SkolskaGodina skolskaGodina,
                                                 @Param("datum") LocalDate datum);

}
