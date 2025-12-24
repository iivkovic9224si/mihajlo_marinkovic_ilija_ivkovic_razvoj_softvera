package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.entity.PolozenPredmet;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolozenPredmetRepository extends JpaRepository<PolozenPredmet, Long> {

    // selekcija svih položenih ispita za broj indeksa studenta
    @Query("select p.predmet " +
            "from PolozenPredmet p " +
            "where p.studentIndeks = :studentIndeks and p.ocena >= 6")
    Page<Predmet> findPolozeniPredmetiByStudentIndeks(StudentIndeks studentIndeks, Pageable pageable);

    //selekcija svih nepoloženih ispita za broj indeksa studenta, paginirano
    @Query("select pr " +
            "from Predmet pr " +
            "where pr in (" +
            "   select sp.drziPredmet.predmet " +
            "   from SlusaPredmet sp " +
            "   where sp.studentIndeks = :studentIndeks" +
            ") " +
            "and pr not in (" +
            "   select pp.predmet " +
            "   from PolozenPredmet pp " +
            "   where pp.studentIndeks = :studentIndeks and pp.ocena >= 6" +
            ")")
    Page<Predmet> findNePolozeniPredmetiByStudentIndeks(StudentIndeks studentIndeks, Pageable pageable);



    //prosečna ocena studenata na predmetu za raspon godina (na primer zadaje se godine 2015-2018 i vraća se prosečna ocena svih studenata koji su položili predmet u tom periodu)
    @Query("select AVG(p.ocena) from PolozenPredmet p where p.ocena >= 6 and p.predmet = :predmet and year(p.datum) between :donjaGodina and :gornjaGodina")
    Double getProsecnaOcenaZaPredmetIzmedju(@Param("predmet") Predmet predmet,
                                            @Param("donjaGodina") Integer donjaGodina,
                                            @Param("gornjaGodina") Integer gornjaGodina);

    @Query("SELECT pp FROM PolozenPredmet pp WHERE pp.studentIndeks = :studentIndeks AND pp.ocena >= 6")
    Page<PolozenPredmet> findPolozeniByStudentIndeks(
            @Param("studentIndeks") StudentIndeks studentIndeks,
            Pageable pageable
    );

    @Query("select coalesce(sum(pp.predmet.espb), 0) " +
            "from PolozenPredmet pp " +
            "where pp.studentIndeks.id = :studentIndeksId and pp.ocena is not null")
    Integer sumEspbZaStudenta(@Param("studentIndeksId") Long studentIndeksId);

    @Query("select coalesce(avg(pp.ocena), 0) " +
            "from PolozenPredmet pp " +
            "where pp.studentIndeks.id = :studentIndeksId and pp.ocena is not null")
    Double avgOcenaZaStudenta(@Param("studentIndeksId") Long studentIndeksId);

}
